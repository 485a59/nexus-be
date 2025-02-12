package cn.edu.nwafu.nexus.domain.component;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.service.MemberService;
import cn.edu.nwafu.nexus.domain.service.ShareFileService;
import cn.edu.nwafu.nexus.domain.service.ShareService;
import cn.edu.nwafu.nexus.domain.util.SystemFileUtils;
import cn.edu.nwafu.nexus.domain.util.TreeNode;
import cn.edu.nwafu.nexus.infrastructure.mapper.FileMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.MusicMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.File;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Share;
import cn.edu.nwafu.nexus.infrastructure.model.entity.ShareFile;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.param.FileSearch;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import cn.edu.nwafu.nexus.ufop.operation.write.Writer;
import cn.edu.nwafu.nexus.ufop.operation.write.domain.WriteFile;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 文件逻辑处理组件。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class FileHandler {
    public static Executor exec = Executors.newFixedThreadPool(20);
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private MemberService memberService;
    @Resource
    private ShareService shareService;
    @Resource
    private ShareFileService shareFileService;
    @Resource
    private UFOPFactory ufopFactory;
    @Resource
    private MusicMapper musicMapper;
    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * 获取重复文件名。<br/>
     * 场景 1: 文件还原时，在 {@code savedPath} 路径下，保存 测试.txt 文件重名，则会生成 测试(1).txt<br/>
     * 场景 2：上传文件时，在 {@code savedPath} 路径下，保存 测试.txt 文件重名，则会生成 测试(1).txt<br/>
     *
     * @param userFile  用户文件
     * @param savedPath 保存的文件路径
     * @return 文件名
     */
    public String getRepeatFileName(UserFile userFile, String savedPath) {
        String fileName = userFile.getFileName();
        String extendName = userFile.getExtension();

        String userId = userFile.getUserId();
        int isDir = userFile.getIsDir();
        QueryWrapper<UserFile> lambdaQueryWrapper = new QueryWrapper<>();
        lambdaQueryWrapper.isNull("delete_time");
        lambdaQueryWrapper.lambda().eq(UserFile::getFilePath, savedPath)
                .eq(UserFile::getUserId, userId)
                .eq(UserFile::getFileName, fileName)
                .eq(UserFile::getIsDir, isDir);
        if (userFile.isFile()) {
            lambdaQueryWrapper.lambda().eq(UserFile::getExtension, extendName);
        }
        List<UserFile> list = userFileMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return fileName;
        }

        int i = 0;
        while (!CollectionUtils.isEmpty(list)) {
            i++;
            QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserFile::getFilePath, savedPath)
                    .eq(UserFile::getUserId, userId)
                    .eq(UserFile::getFileName, fileName + "(" + i + ")")
                    .eq(UserFile::getIsDir, isDir);
            queryWrapper.isNull("delete_time");
            if (userFile.isFile()) {
                queryWrapper.lambda().eq(UserFile::getExtension, extendName);
            }
            list = userFileMapper.selectList(queryWrapper);
        }
        return fileName + "(" + i + ")";

    }

    /**
     * 还原父文件路径。<br/>
     * 1. 回收站文件还原操作会将文件恢复到原来的路径下,当还原文件的时候，如果父目录已经不存在了，则需要把父母录给还原。</br>
     * 2. 上传目录
     *
     * @param systemFile    系统文件
     * @param sessionUserId 用户 ID
     */
    public void restoreParentFilePath(SystemFile systemFile, String sessionUserId) {
        if (systemFile.isFile()) {
            systemFile = systemFile.getParentFile();
        }
        while (systemFile.getParent() != null) {
            String fileName = systemFile.getName();
            String parentFilePath = systemFile.getParent();

            QueryWrapper<UserFile> lambdaQueryWrapper = new QueryWrapper<>();
            lambdaQueryWrapper.lambda().eq(UserFile::getFilePath, parentFilePath)
                    .eq(UserFile::getFileName, fileName)
                    .eq(UserFile::getIsDir, 1)
                    .eq(UserFile::getUserId, sessionUserId);
            lambdaQueryWrapper.isNull("delete_time");
            List<UserFile> userFileList = userFileMapper.selectList(lambdaQueryWrapper);
            if (userFileList.isEmpty()) {
                UserFile userFile = SystemFileUtils.getDir(sessionUserId, parentFilePath, fileName);
                try {
                    userFileMapper.insert(userFile);
                } catch (Exception e) {
                    // ignore
                }
            }
            systemFile = new SystemFile(parentFilePath, true);
        }
    }


    /**
     * 删除重复的子目录文件
     * <p>
     * 当还原目录的时候，如果其子目录在文件系统中已存在，则还原之后进行去重操作。
     */
    public void deleteRepeatSubDirFile(String filePath, String sessionUserId) {
        log.debug("删除子目录：" + filePath);
        QueryWrapper<UserFile> lambdaQueryWrapper = new QueryWrapper<>();
        lambdaQueryWrapper.isNull("delete_time");
        lambdaQueryWrapper.lambda().select(UserFile::getFileName, UserFile::getFilePath)
                .likeRight(UserFile::getFilePath, SystemFileUtils.formatLikePath(filePath))
                .eq(UserFile::getIsDir, 1)
                .eq(UserFile::getUserId, sessionUserId)
                .groupBy(UserFile::getFilePath, UserFile::getFileName)
                .having("count(file_name) >= 2");
        List<UserFile> repeatList = userFileMapper.selectList(lambdaQueryWrapper);

        for (UserFile userFile : repeatList) {
            LambdaQueryWrapper<UserFile> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(UserFile::getFilePath, userFile.getFilePath())
                    .eq(UserFile::getFileName, userFile.getFileName())
                    .isNull(UserFile::getDeleteTime);
            List<UserFile> userFiles = userFileMapper.selectList(lambdaQueryWrapper1);
            for (int i = 0; i < userFiles.size() - 1; i++) {
                userFileMapper.deleteById(userFiles.get(i).getId());
            }
        }
    }

    /**
     * 组织一个树目录节点，文件移动的时候使用。
     */
    public TreeNode insertTreeNode(TreeNode treeNode, long id, String filePath, Queue<String> nodeNameQueue) {
        List<TreeNode> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return treeNode;
        }

        SystemFile systemFile = new SystemFile(filePath, currentNodeName, true);
        filePath = systemFile.getPath();

        if (!isExistPath(childrenTreeNodes, currentNodeName)) {  //1、判断有没有该子节点，如果没有则插入
            // 插入
            TreeNode resultTreeNode = new TreeNode();
            resultTreeNode.setFilePath(filePath);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            resultTreeNode.setId(++id);

            childrenTreeNodes.add(resultTreeNode);

        } else {  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (!nodeNameQueue.isEmpty()) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                TreeNode childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    childrenTreeNode = insertTreeNode(childrenTreeNode, id * 10, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }

            }
        } else {
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;

    }

    /**
     * 判断该路径在树节点中是否已经存在。
     */
    public boolean isExistPath(List<TreeNode> childrenTreeNodes, String path) {
        boolean isExistPath = false;

        try {
            for (TreeNode childrenTreeNode : childrenTreeNodes) {
                if (path.equals(childrenTreeNode.getLabel())) {
                    isExistPath = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return isExistPath;
    }


    public void uploadESByUserFileId(String userFileId) {
        exec.execute(() -> {
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("userFileId", userFileId);
                List<UserFile> userfileResult = userFileMapper.selectByMap(param);
                if (userfileResult != null && !userfileResult.isEmpty()) {
                    FileSearch fileSearch = new FileSearch();
                    BeanUtil.copyProperties(userfileResult.get(0), fileSearch);
                    elasticsearchClient.index(i -> i.index("filesearch").id(fileSearch.getUserFileId()).document(fileSearch));
                }
            } catch (Exception e) {
                log.debug("ES更新操作失败，请检查配置");
            }
        });


    }

    public void deleteESByUserFileId(String userFileId) {
        exec.execute(() -> {
            try {
                elasticsearchClient.delete(d -> d
                        .index("filesearch")
                        .id(userFileId));
            } catch (Exception e) {
                log.debug("ES删除操作失败，请检查配置");
            }
        });


    }

    /**
     * 根据用户传入的参数，判断是否有下载或者预览权限。
     */
    public boolean checkAuthDownloadAndPreview(String shareBatchNum,
                                               String extractionCode,
                                               String token,
                                               List<String> userFileIds,
                                               Integer platform) {
        log.debug("权限检查开始：shareBatchNum:{}, extractionCode:{}, token:{}, userFileIds{}", shareBatchNum, extractionCode, token, userFileIds);
        if (platform != null && platform == 2) {
            return true;
        }
        for (String userFileId : userFileIds) {
            UserFile userFile = userFileMapper.selectById(userFileId);
            log.debug(JSON.toJSONString(userFile));
            if ("undefined".equals(shareBatchNum) || StrUtil.isEmpty(shareBatchNum)) {
                String userId = SessionUtils.getUserId();
                log.debug(JSON.toJSONString("当前登录session用户id：" + userId));
                if (userId == null) {
                    return false;
                }
                log.debug("文件所属用户 id：" + userFile.getUserId());
                log.debug("登录用户 id:" + userId);
                if (!userFile.getUserId().equals(userId)) {
                    log.info("用户id不一致，权限校验失败");
                    return false;
                }
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("shareBatchNum", shareBatchNum);
                List<Share> shareList = shareService.listByMap(param);
                // 判断批次号
                if (shareList.isEmpty()) {
                    log.info("分享批次号不存在，权限校验失败");
                    return false;
                }
                Integer shareType = shareList.get(0).getShareType();
                if (1 == shareType) {
                    // 判断提取码
                    if (!shareList.get(0).getExtractionCode().equals(extractionCode)) {
                        log.info("提取码错误，权限校验失败");
                        return false;
                    }
                }
                param.put("userFileId", userFileId);
                List<ShareFile> shareFileList = shareFileService.listByMap(param);
                if (shareFileList.size() <= 0) {
                    log.info("用户id和分享批次号不匹配，权限校验失败");
                    return false;
                }

            }

        }
        return true;
    }

    /**
     * 拷贝文件
     * 场景：修改的文件被多处引用时，需要重新拷贝一份，然后在新的基础上修改。
     */
    public String copyFile(File file, UserFile userFile) {
        Copier copier = ufopFactory.getCopier();
        Downloader downloader = ufopFactory.getDownloader(file.getStorageType());
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(file.getUrl());
        CopyFile copyFile = new CopyFile();
        copyFile.setExtension(userFile.getExtension());
        String fileUrl = copier.copy(downloader.getInputStream(downloadFile), copyFile);
        if (downloadFile.getOssClient() != null) {
            downloadFile.getOssClient().shutdown();
        }
        file.setUrl(fileUrl);
        fileMapper.insert(file);
        userFile.setFileId(file.getId());
        userFile.setUploadTime(new Date());
        userFile.setModifyUserId(SessionUtils.getUserId());
        userFileMapper.updateById(userFile);
        return fileUrl;
    }

    public String getIdentifierByFile(String fileUrl, int storageType) throws IOException {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(fileUrl);
        InputStream inputStream = ufopFactory.getDownloader(storageType).getInputStream(downloadFile);
        return DigestUtils.md5Hex(inputStream);
    }

    public void saveFileInputStream(int storageType, String fileUrl, InputStream inputStream) throws IOException {
        Writer writer = ufopFactory.getWriter(storageType);
        WriteFile writeFile = new WriteFile();
        writeFile.setFileUrl(fileUrl);
        int fileSize = inputStream.available();
        writeFile.setFileSize(fileSize);
        writer.write(inputStream, writeFile);
    }

    public boolean isDirExist(String fileName, String filePath, String userId) {
        QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileName)
                .eq("file_path", SystemFile.formatPath(filePath))
                .eq("user_id", userId)
                .isNull("delete_time")
                .eq("is_dir", 1);
        List<UserFile> list = userFileMapper.selectList(queryWrapper);
        return list != null && !list.isEmpty();
    }
}
