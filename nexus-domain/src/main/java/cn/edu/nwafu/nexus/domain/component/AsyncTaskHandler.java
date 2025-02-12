package cn.edu.nwafu.nexus.domain.component;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.domain.service.RecoveryFileService;
import cn.edu.nwafu.nexus.infrastructure.mapper.FileMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 异步任务业务类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
@Async("asyncTaskExecutor")
public class AsyncTaskHandler {
    @Resource
    RecoveryFileService recoveryFileService;
    @Resource
    FileTransferService fileTransferService;
    @Resource
    UFOPFactory ufopFactory;
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    FileHandler fileHandler;

    @Value("${ufop.storage-type}")
    private Integer storageType;

    public Long getFilePointCount(String fileId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileId, fileId);
        return userFileMapper.selectCount(lambdaQueryWrapper);
    }

    public Future<String> deleteUserFile(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        if (userFile.getIsDir() == 1) {
            LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userFileLambdaQueryWrapper.eq(UserFile::getDeleteBatchNum, userFile.getDeleteBatchNum());
            List<UserFile> list = userFileMapper.selectList(userFileLambdaQueryWrapper);
            recoveryFileService.deleteUserFileByDeleteBatchNum(userFile.getDeleteBatchNum());
            for (UserFile userFileItem : list) {
                Long filePointCount = getFilePointCount(userFileItem.getFileId());

                if (filePointCount != null && filePointCount == 0 && userFileItem.getIsDir() == 0) {
                    cn.edu.nwafu.nexus.infrastructure.model.entity.File file = fileMapper.selectById(userFileItem.getFileId());
                    if (file != null) {
                        try {
                            fileTransferService.deleteFile(file);
                            fileMapper.deleteById(file.getId());
                        } catch (Exception e) {
                            log.error("删除本地文件失败：" + JSON.toJSONString(file));
                        }
                    }


                }
            }
        } else {

            recoveryFileService.deleteUserFileByDeleteBatchNum(userFile.getDeleteBatchNum());
            Long filePointCount = getFilePointCount(userFile.getFileId());

            if (filePointCount != null && filePointCount == 0 && userFile.getIsDir() == 0) {
                cn.edu.nwafu.nexus.infrastructure.model.entity.File file = fileMapper.selectById(userFile.getFileId());
                try {
                    fileTransferService.deleteFile(file);
                    fileMapper.deleteById(file.getId());
                } catch (Exception e) {
                    log.error("删除本地文件失败：" + JSON.toJSONString(file));
                }
            }
        }

        return new AsyncResult<>("deleteUserFile");
    }

    public Future<String> checkESUserFileId(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        if (userFile == null) {
            fileHandler.deleteESByUserFileId(userFileId);
        }
        return new AsyncResult<>("checkUserFileId");
    }


    public Future<String> saveUnzipFile(UserFile userFile, cn.edu.nwafu.nexus.infrastructure.model.entity.File fileBean, int unzipMode, String entryName, String filePath) {
        String unzipUrl = UFOPUtils.getTempFile(fileBean.getUrl()).getAbsolutePath().replace("." + userFile.getExtension(), "");
        String totalFileUrl = unzipUrl + entryName;
        File currentFile = new File(totalFileUrl);

        String fileId = null;
        if (!currentFile.isDirectory()) {
            FileInputStream fis = null;
            String md5Str = UUID.randomUUID().toString();
            try {
                fis = new FileInputStream(currentFile);
                md5Str = DigestUtils.md5Hex(fis);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(fis);
            }

            FileInputStream fileInputStream = null;
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("identifier", md5Str);
                List<cn.edu.nwafu.nexus.infrastructure.model.entity.File> list = fileMapper.selectByMap(param);

                if (list != null && !list.isEmpty()) { //文件已存在
                    fileId = list.get(0).getId();
                } else {
                    // 文件不存在
                    fileInputStream = new FileInputStream(currentFile);
                    CopyFile createFile = new CopyFile();
                    createFile.setExtension(FilenameUtils.getExtension(totalFileUrl));
                    String saveFileUrl = ufopFactory.getCopier().copy(fileInputStream, createFile);

                    cn.edu.nwafu.nexus.infrastructure.model.entity.File tempFileBean = new cn.edu.nwafu.nexus.infrastructure.model.entity.File(saveFileUrl, currentFile.length(), storageType, md5Str, userFile.getUserId());

                    fileMapper.insert(tempFileBean);
                    fileId = tempFileBean.getId();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(fileInputStream);
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                boolean success = currentFile.delete();
                if (!success) {
                    log.error("删除文件 {} 失败", currentFile.getName());
                }
            }
        }

        SystemFile systemFile = null;
        if (unzipMode == 0) {
            systemFile = new SystemFile(userFile.getFilePath(), entryName, currentFile.isDirectory());
        } else if (unzipMode == 1) {
            systemFile = new SystemFile(userFile.getFilePath() + "/" + userFile.getFileName(), entryName, currentFile.isDirectory());
        } else if (unzipMode == 2) {
            systemFile = new SystemFile(filePath, entryName, currentFile.isDirectory());
        } else {
            log.error("不支持的解压类型");
            return null;
        }
        UserFile saveUserFile = new UserFile(systemFile, userFile.getUserId(), fileId);
        String fileName = fileHandler.getRepeatFileName(saveUserFile, saveUserFile.getFilePath());

        if (saveUserFile.getIsDir() == 1 && !fileName.equals(saveUserFile.getFileName())) {
            // 如果是目录，而且重复，什么也不做
        } else {
            saveUserFile.setFileName(fileName);
            userFileMapper.insert(saveUserFile);
        }
        fileHandler.restoreParentFilePath(systemFile, userFile.getUserId());

        return new AsyncResult<>("saveUnzipFile");
    }
}
