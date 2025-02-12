package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.infrastructure.mapper.RecoveryFileMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.RecoveryFile;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLDecoder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {
    public static Executor executor = Executors.newFixedThreadPool(20);
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    RecoveryFileMapper recoveryFileMapper;
    @Resource
    FileHandler fileHandler;

    @Override
    public List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, String userId) {
        QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileName)
                .eq("file_path", filePath)
                .eq("user_id", userId)
                .isNull("delete_time");
        return userFileMapper.selectList(queryWrapper);
    }

    @Override
    public List<UserFile> selectSameUserFile(String fileName, String filePath, String extendName, String userId) {
        QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileName)
                .eq("file_path", filePath)
                .eq("user_id", userId)
                .eq("extension", extendName)
                .isNull("delete_time");
        return userFileMapper.selectList(queryWrapper);
    }


    @Override
    public List<FileListVo> userFileList(String userId, String filePath, Long currentPage, Long pageCount) {
        PageHelper.startPage(currentPage.intValue(), pageCount.intValue());

        UserFile userFile = new UserFile();
        String sessionUserId = SessionUtils.getUserId();
        if (userId == null) {
            userFile.setUserId(sessionUserId);
        } else {
            userFile.setUserId(userId);
        }

        userFile.setFilePath(URLDecoder.decodeForPath(filePath, StandardCharsets.UTF_8));

        return userFileMapper.selectPageVo(userFile, null);
    }

    @Override
    public void updateFilepathByUserFileId(String userFileId, String newFilePath, String userId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String oldFilePath = userFile.getFilePath();
        String fileName = userFile.getFileName();

        userFile.setFilePath(newFilePath);
        if (userFile.getIsDir() == 0) {
            String repeatFileName = fileHandler.getRepeatFileName(userFile, userFile.getFilePath());
            userFile.setFileName(repeatFileName);
        }
        try {
            userFileMapper.updateById(userFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        //移动子目录
        oldFilePath = new SystemFile(oldFilePath, fileName, true).getPath();
        newFilePath = new SystemFile(newFilePath, fileName, true).getPath();

        if (userFile.isDirectory()) { //如果是目录，则需要移动子目录
            List<UserFile> list = selectUserFileByLikeRightFilePath(oldFilePath, userId);

            for (UserFile newUserFile : list) {
                newUserFile.setFilePath(newUserFile.getFilePath().replaceFirst(oldFilePath, newFilePath));
                if (newUserFile.getIsDir() == 0) {
                    String repeatFileName = fileHandler.getRepeatFileName(newUserFile, newUserFile.getFilePath());
                    newUserFile.setFileName(repeatFileName);
                }
                try {
                    userFileMapper.updateById(newUserFile);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        }

    }

    @Override
    public void userFileCopy(String userId, String userFileId, String newFilePath) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String oldFilePath = userFile.getFilePath();
        String oldUserId = userFile.getUserId();
        String fileName = userFile.getFileName();
        userFile.setFilePath(newFilePath);
        userFile.setUserId(userId);
        // 覆盖原有 id
        userFile.setId(null);
        if (userFile.getIsDir() == 0) {
            String repeatFileName = fileHandler.getRepeatFileName(userFile, userFile.getFilePath());
            userFile.setFileName(repeatFileName);
        }
        try {
            userFileMapper.insert(userFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        oldFilePath = new SystemFile(oldFilePath, fileName, true).getPath();
        newFilePath = new SystemFile(newFilePath, fileName, true).getPath();


        if (userFile.isDirectory()) {
            List<UserFile> subUserFileList = userFileMapper.selectUserFileByLikeRightFilePath(oldFilePath, oldUserId);

            for (UserFile newUserFile : subUserFileList) {
                newUserFile.setFilePath(newUserFile.getFilePath().replaceFirst(oldFilePath, newFilePath));
                if (newUserFile.isDirectory()) {
                    String repeatFileName = fileHandler.getRepeatFileName(newUserFile, newUserFile.getFilePath());
                    newUserFile.setFileName(repeatFileName);
                }
                newUserFile.setUserId(userId);
                try {
                    userFileMapper.insert(newUserFile);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        }

    }

    @Override
    public List<FileListVo> getFileByFileType(Integer fileTypeId, Long currentPage, Long pageCount, String userId) {
        PageHelper.startPage(currentPage.intValue(), pageCount.intValue());
        UserFile userFile = new UserFile();
        userFile.setUserId(userId);
        return userFileMapper.selectPageVo(userFile, fileTypeId);
    }

    @Override
    public List<UserFile> selectUserFileListByPath(String filePath, String userId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(UserFile::getFilePath, filePath)
                .eq(UserFile::getUserId, userId)
                .isNull(UserFile::getDeleteTime);
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<UserFile> selectFilePathTreeByUserId(String userId) {
        QueryWrapper<UserFile> lambdaQueryWrapper = new QueryWrapper<>();
        lambdaQueryWrapper.lambda().eq(UserFile::getUserId, userId)
                .eq(UserFile::getIsDir, 1);
        lambdaQueryWrapper.isNull("delete_time");
        return userFileMapper.selectList(lambdaQueryWrapper);
    }


    @Override
    public void deleteUserFile(String userFileId, String sessionUserId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String uuid = UUID.randomUUID().toString();
        if (userFile.getIsDir() == 1) {
            UpdateWrapper<UserFile> userFileUpdateWrapper = new UpdateWrapper<>();
            userFileUpdateWrapper
                    .set("delete_batch_num", uuid)
                    .set("delete_time", new Date())
                    .eq("user_file_id", userFileId);
            userFileMapper.update(null, userFileUpdateWrapper);

            String filePath = new SystemFile(userFile.getFilePath(), userFile.getFileName(), true).getPath();
            updateFileDeleteStateByFilePath(filePath, uuid, sessionUserId);

        } else {
            UpdateWrapper<UserFile> userFileUpdateWrapper = new UpdateWrapper<>();
            userFileUpdateWrapper
                    .set("delete_time", new Date())
                    .set("delete_batch_num", uuid)
                    .eq("id", userFileId);
            userFileMapper.update(null, userFileUpdateWrapper);
        }

        RecoveryFile recoveryFile = new RecoveryFile();
        recoveryFile.setUserFileId(userFileId);
        recoveryFile.setDeleteTime(new Date());
        recoveryFile.setDeleteBatchNum(uuid);
        recoveryFileMapper.insert(recoveryFile);
    }

    @Override
    public List<UserFile> selectUserFileByLikeRightFilePath(String filePath, String userId) {
        return userFileMapper.selectUserFileByLikeRightFilePath(filePath, userId);
    }

    private void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, String userId) {
        executor.execute(() -> {
            List<UserFile> fileList = selectUserFileByLikeRightFilePath(filePath, userId);
            List<String> userFileIds = fileList.stream().map(UserFile::getId).collect(Collectors.toList());

            // 标记删除时间
            if (CollUtil.isNotEmpty(userFileIds)) {
                LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                userFileLambdaUpdateWrapper1
                        .set(UserFile::getDeleteTime, new Date())
                        .set(UserFile::getDeleteBatchNum, deleteBatchNum)
                        .in(UserFile::getId, userFileIds)
                        .isNull(UserFile::getDeleteTime);
                userFileMapper.update(null, userFileLambdaUpdateWrapper1);
            }
            for (String userFileId : userFileIds) {
                fileHandler.deleteESByUserFileId(userFileId);
            }
        });
    }
}
