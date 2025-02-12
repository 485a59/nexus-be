package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.common.exception.Asserts;
import cn.edu.nwafu.nexus.common.util.MimeUtils;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.service.FileService;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.infrastructure.mapper.*;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.BatchDownloadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.DownloadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.PreviewDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.File;
import cn.edu.nwafu.nexus.infrastructure.model.entity.*;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.UploadFileVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import cn.edu.nwafu.nexus.ufop.exception.operation.DownloadException;
import cn.edu.nwafu.nexus.ufop.exception.operation.UploadException;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.Range;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileTransferServiceImpl implements FileTransferService {
    @Resource
    private Executor fileTransferExecutor;
    @Lazy
    @Resource
    private FileHandler fileHandler;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private UFOPFactory ufopFactory;
    @Resource
    private UploadTaskDetailMapper uploadTaskDetailMapper;
    @Resource
    private UploadTaskMapper uploadTaskMapper;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private PictureFileMapper pictureFileMapper;
    @Resource
    private UserFileService userFileService;
    @Resource
    private FileService fileService;

    @NotNull
    private static UploadTask getUploadTask(UploadFileDto uploadFileDto, SystemFile systemFile) {
        UploadTask uploadTask = new UploadTask();
        uploadTask.setIdentifier(uploadFileDto.getIdentifier());
        uploadTask.setUploadTime(new Date());
        uploadTask.setUploadStatus(UploadFileStatusEnum.UNCOMPLETED.getCode());
        uploadTask.setFileName(systemFile.getNameNotExtend());
        uploadTask.setFilePath(systemFile.getParent());
        uploadTask.setExtension(systemFile.getExtension());
        uploadTask.setUserId(SessionUtils.getUserId());
        return uploadTask;
    }

    @NotNull
    private static UploadTaskDetail getUploadTaskDetail(UploadFileDto uploadFileDto, SystemFile systemFile) {
        UploadTaskDetail uploadTaskDetail = new UploadTaskDetail();
        uploadTaskDetail.setFilePath(systemFile.getParent());
        uploadTaskDetail.setFileName(systemFile.getNameNotExtend());
        uploadTaskDetail.setChunkNumber(uploadFileDto.getChunkNumber());
        uploadTaskDetail.setChunkSize((int) uploadFileDto.getChunkSize());
        uploadTaskDetail.setRelativePath(uploadFileDto.getRelativePath());
        uploadTaskDetail.setTotalChunks(uploadFileDto.getTotalChunks());
        uploadTaskDetail.setTotalSize((int) uploadFileDto.getTotalSize());
        uploadTaskDetail.setIdentifier(uploadFileDto.getIdentifier());
        return uploadTaskDetail;
    }

    private void asyncRestoreParentFilePath(SystemFile systemFile, String userId) {
        fileTransferExecutor.execute(() -> {
            fileHandler.restoreParentFilePath(systemFile, userId);
        });
    }

    @Override
    public UploadFileVo uploadFileSpeed(UploadFileDto uploadFileDto) {
        UploadFileVo uploadFileVo = new UploadFileVo();

        // 1. 先检查文件是否已存在
        Map<String, Object> param = new HashMap<>();
        param.put("identifier", uploadFileDto.getIdentifier());
        List<File> list = fileMapper.selectByMap(param);

        String filePath = uploadFileDto.getPath();
        String relativePath = uploadFileDto.getRelativePath();
        SystemFile systemFile;
        if (relativePath.contains("/")) {
            systemFile = new SystemFile(filePath, relativePath, false);
        } else {
            systemFile = new SystemFile(filePath, uploadFileDto.getName(), false);
        }

        if (list != null && !list.isEmpty()) {
            cn.edu.nwafu.nexus.infrastructure.model.entity.File file = list.get(0);
            UserFile userFile = new UserFile(systemFile, SessionUtils.getUserId(), file.getId());
            try {
                userFileMapper.insert(userFile);
                fileHandler.uploadESByUserFileId(userFile.getFileId());
            } catch (Exception e) {
                log.warn("极速上传文件冲突重命名处理: {}", JSON.toJSONString(userFile));
            }

            if (relativePath.contains("/")) {
                asyncRestoreParentFilePath(systemFile, SessionUtils.getUserId());
            }
            uploadFileVo.setSkipUpload(true);
        } else {
            uploadFileVo.setSkipUpload(false);

            // 2. 检查上传任务状态
            LambdaQueryWrapper<UploadTask> taskWrapper = new LambdaQueryWrapper<>();
            taskWrapper.eq(UploadTask::getIdentifier, uploadFileDto.getIdentifier());
            UploadTask existingTask = uploadTaskMapper.selectOne(taskWrapper);

            if (existingTask != null) {
                // 如果任务存在且未完成，检查已上传的分片
                if (UploadFileStatusEnum.UNCOMPLETED.getCode().equals(existingTask.getUploadStatus())) {
                    List<Integer> uploaded = uploadTaskDetailMapper.selectUploadedChunkNumList(uploadFileDto.getIdentifier());
                    if (uploaded != null && !uploaded.isEmpty()) {
                        uploadFileVo.setUploaded(uploaded);
                    }
                }
                // 如果任务已失败，重新创建任务
                else if (UploadFileStatusEnum.FAILED.getCode().equals(existingTask.getUploadStatus())) {
                    uploadTaskMapper.deleteById(existingTask.getId());
                    UploadTask newTask = getUploadTask(uploadFileDto, systemFile);
                    uploadTaskMapper.insert(newTask);
                }
            } else {
                // 3. 创建新的上传任务
                UploadTask newTask = getUploadTask(uploadFileDto, systemFile);
                uploadTaskMapper.insert(newTask);
            }
        }
        return uploadFileVo;
    }

    @Override
    public void uploadFile(HttpServletRequest request, UploadFileDto uploadFileDto, String userId) {
        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDto.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDto.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDto.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDto.getIdentifier());
        uploadFile.setTotalSize(uploadFileDto.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDto.getCurrentChunkSize());

        Uploader uploader = ufopFactory.getUploader();
        if (uploader == null) {
            log.error("上传失败，请检查存储类型是否配置正确");
            Asserts.fail("上传失败，请检查存储类型配置");
            throw new UploadException("上传失败");
        }

        List<UploadFileResult> uploadFileResultList = new ArrayList<>();
        try {
            uploadFileResultList = uploader.upload(request, uploadFile);
        } catch (Exception e) {
            log.error("上传失败，请检查 UFOP 连接配置是否正确", e);
            Asserts.fail("上传失败，请检查 UFOP 连接配置");
        }

        for (UploadFileResult uploadFileResult : uploadFileResultList) {
            String relativePath = uploadFileDto.getRelativePath();
            SystemFile systemFile;
            if (relativePath.contains("/")) {
                systemFile = new SystemFile(uploadFileDto.getPath(), relativePath, false);
            } else {
                systemFile = new SystemFile(uploadFileDto.getPath(), uploadFileDto.getName(), false);
            }

            // 检查当前上传任务状态
            LambdaQueryWrapper<UploadTask> taskWrapper = new LambdaQueryWrapper<>();
            taskWrapper.eq(UploadTask::getIdentifier, uploadFileDto.getIdentifier());
            UploadTask existingTask = uploadTaskMapper.selectOne(taskWrapper);

            // 如果任务不存在，创建新任务
            if (existingTask == null) {
                existingTask = getUploadTask(uploadFileDto, systemFile);
                uploadTaskMapper.insert(existingTask);
            }

            if (UploadFileStatusEnum.SUCCESS.equals(uploadFileResult.getStatus())) {
                // 处理文件上传成功的情况
                cn.edu.nwafu.nexus.infrastructure.model.entity.File file = new cn.edu.nwafu.nexus.infrastructure.model.entity.File(uploadFileResult);
                file.setCreateUserId(userId);
                String fileId = null;
                try {
                    fileMapper.insert(file);
                    fileId = file.getId();
                } catch (Exception e) {
                    log.warn("identifier Duplicate: {}", file.getIdentifier());
                    file = fileMapper.selectOne(new QueryWrapper<cn.edu.nwafu.nexus.infrastructure.model.entity.File>().lambda()
                            .eq(cn.edu.nwafu.nexus.infrastructure.model.entity.File::getIdentifier, file.getIdentifier()));
                }

                UserFile userFile = new UserFile(systemFile, userId, file.getId());
                try {
                    userFileMapper.insert(userFile);
                    fileHandler.uploadESByUserFileId(fileId);
                } catch (Exception e) {
                    UserFile existingUserFile = userFileMapper.selectOne(new QueryWrapper<UserFile>().lambda()
                            .eq(UserFile::getUserId, userFile.getUserId())
                            .eq(UserFile::getFilePath, userFile.getFilePath())
                            .eq(UserFile::getFileName, userFile.getFileName())
                            .eq(UserFile::getExtension, userFile.getExtension())
                            .isNull(UserFile::getDeleteTime)
                            .eq(UserFile::getIsDir, userFile.getIsDir()));

                    if (existingUserFile != null) {
                        cn.edu.nwafu.nexus.infrastructure.model.entity.File file1 = fileMapper.selectById(existingUserFile.getFileId());
                        if (!StrUtil.equals(file.getIdentifier(), file1.getIdentifier())) {
                            log.warn("文件冲突重命名处理: {}", JSON.toJSONString(existingUserFile));
                            String fileName = fileHandler.getRepeatFileName(userFile, userFile.getFilePath());
                            userFile.setFileName(fileName);
                            userFileMapper.insert(userFile);
                            fileId = userFile.getFileId();  // 获取重命名后插入的ID
                            fileHandler.uploadESByUserFileId(fileId);
                        } else {
                            fileId = existingUserFile.getId();  // 使用已存在文件的ID
                        }
                    }
                }

                // 确保获取到了userFileId后再更新上传任务
                if (fileId != null) {
                    // 更新任务状态为成功，并设置 userFileId
                    existingTask.setUploadStatus(UploadFileStatusEnum.SUCCESS.getCode());
                    existingTask.setFileId(fileId);
                    uploadTaskMapper.updateById(existingTask);
                } else {
                    log.error("Failed to get userFileId for file: {}", JSON.toJSONString(userFile));
                }

                // 清理分片记录
                uploadTaskDetailMapper.delete(new LambdaQueryWrapper<UploadTaskDetail>()
                        .eq(UploadTaskDetail::getIdentifier, uploadFileDto.getIdentifier()));

                // 处理相关路径和图片
                if (relativePath.contains("/")) {
                    asyncRestoreParentFilePath(systemFile, userId);
                }
                handleImageFile(uploadFileResult, file);

            } else if (UploadFileStatusEnum.UNCOMPLETED.equals(uploadFileResult.getStatus())) {
                // 处理未完成的分片上传
                UploadTaskDetail uploadTaskDetail = getUploadTaskDetail(uploadFileDto, systemFile);
                uploadTaskDetailMapper.insert(uploadTaskDetail);

                // 确保任务状态为未完成
                if (!UploadFileStatusEnum.UNCOMPLETED.getCode().equals(existingTask.getUploadStatus())) {
                    existingTask.setUploadStatus(UploadFileStatusEnum.UNCOMPLETED.getCode());
                    uploadTaskMapper.updateById(existingTask);
                }

            } else if (UploadFileStatusEnum.FAILED.equals(uploadFileResult.getStatus())) {
                // 处理上传失败的情况
                uploadTaskDetailMapper.delete(new LambdaQueryWrapper<UploadTaskDetail>()
                        .eq(UploadTaskDetail::getIdentifier, uploadFileDto.getIdentifier()));

                existingTask.setUploadStatus(UploadFileStatusEnum.FAILED.getCode());
                uploadTaskMapper.updateById(existingTask);
            }
        }
    }

    // 处理图片文件的辅助方法
    private void handleImageFile(UploadFileResult uploadFileResult, cn.edu.nwafu.nexus.infrastructure.model.entity.File file) {
        try {
            if (UFOPUtils.isImageFile(uploadFileResult.getExtension())) {
                BufferedImage src = uploadFileResult.getBufferedImage();
                Image image = new Image();
                image.setWidth(src.getWidth());
                image.setHeight(src.getHeight());
                image.setFileId(file.getId());
                imageMapper.insert(image);
            }
        } catch (Exception e) {
            log.error("生成图片缩略图失败", e);
        }
    }

    @Override
    public void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDto downloadFileDTO) {
        UserFile userFile = userFileMapper.selectById(downloadFileDTO.getId());

        if (userFile.isFile()) {

            cn.edu.nwafu.nexus.infrastructure.model.entity.File file = fileMapper.selectById(userFile.getFileId());
            Downloader downloader = ufopFactory.getDownloader(file.getStorageType());
            if (downloader == null) {
                log.info("下载失败，文件存储类型不支持下载，storageType: {}", file.getStorageType());
                Asserts.fail("下载失败，文件存储类型不支持下载");
                throw new DownloadException("下载失败");
            }
            DownloadFile downloadFile = new DownloadFile();

            downloadFile.setFileUrl(file.getUrl());
            httpServletResponse.setContentLengthLong(file.getSize());
            downloader.download(httpServletResponse, downloadFile);
        } else {
            SystemFile systemFile = new SystemFile(userFile.getFilePath(), userFile.getFileName(), true);
            List<UserFile> userFileList = userFileMapper.selectUserFileByLikeRightFilePath(systemFile.getPath(), userFile.getUserId());
            List<String> userFileIds = userFileList.stream().map(UserFile::getId).collect(Collectors.toList());

            downloadUserFileList(httpServletResponse, userFile.getFilePath(), userFile.getFileName(), userFileIds);
        }
    }

    @Override
    public void downloadUserFileList(HttpServletResponse httpServletResponse, String filePath, String fileName, List<String> userFileIds) {
        String staticPath = UFOPUtils.getStaticPath();
        String tempPath = staticPath + "temp" + java.io.File.separator;
        java.io.File tempDirFile = new java.io.File(tempPath);
        if (!tempDirFile.exists()) {
            boolean success = tempDirFile.mkdirs();
            if (!success) {
                log.error("创建文件夹 {} 失败", tempPath);
            }
        }
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(tempPath + fileName + ".zip");
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream zos = new ZipOutputStream(csum);
        BufferedOutputStream out = new BufferedOutputStream(zos);

        try {
            for (String userFileId : userFileIds) {
                UserFile userFile1 = userFileMapper.selectById(userFileId);
                if (userFile1.isFile()) {
                    cn.edu.nwafu.nexus.infrastructure.model.entity.File fileBean = fileMapper.selectById(userFile1.getFileId());
                    Downloader downloader = ufopFactory.getDownloader(fileBean.getStorageType());
                    if (downloader == null) {
                        log.error("下载失败，文件存储类型不支持下载，storageType:{}", fileBean.getStorageType());
                        throw new UploadException("下载失败");
                    }
                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.setFileUrl(fileBean.getUrl());
                    InputStream inputStream = downloader.getInputStream(downloadFile);
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    try {
                        SystemFile systemFile = new SystemFile(StrUtil.removePrefix(userFile1.getFilePath(), filePath), userFile1.getFileName() + "." + userFile1.getExtension(), false);
                        zos.putNextEntry(new ZipEntry(systemFile.getPath()));

                        byte[] buffer = new byte[1024];
                        int i = bis.read(buffer);
                        while (i != -1) {
                            out.write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        IOUtils.closeQuietly(bis);
                        try {
                            out.flush();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                } else {
                    SystemFile systemFile = new SystemFile(StrUtil.removePrefix(userFile1.getFilePath(), filePath), userFile1.getFileName(), true);
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(systemFile.getPath() + SystemFile.separator));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }

        } catch (Exception e) {
            log.error("压缩过程中出现异常", e);
            Asserts.fail("压缩过程中出现异常");
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        String zipPath = "";
        try {
            Downloader downloader = ufopFactory.getDownloader(StorageTypeEnum.LOCAL.getCode());
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.setFileUrl("temp" + java.io.File.separator + fileName + ".zip");
            java.io.File tempFile = new java.io.File(UFOPUtils.getStaticPath() + downloadFile.getFileUrl());
            httpServletResponse.setContentLengthLong(tempFile.length());
            downloader.download(httpServletResponse, downloadFile);
            zipPath = UFOPUtils.getStaticPath() + "temp" + java.io.File.separator + fileName + ".zip";
        } catch (Exception e) {
            //org.apache.catalina.connector.ClientAbortException: java.io.IOException: Connection reset by peer
            if (!e.getMessage().contains("ClientAbortException")) {
                log.error("下传 zip 文件出现异常：{}", e.getMessage());
            }
        } finally {
            java.io.File file = new java.io.File(zipPath);
            if (file.exists()) {
                boolean success = file.delete();
                if (!success) {
                    log.error("删除文件 {} 失败", zipPath);
                }
            }
        }
    }

    @Override
    public void previewFile(HttpServletResponse httpServletResponse, PreviewDto previewDto) {
        UserFile userFile = userFileMapper.selectById(previewDto.getId());
        cn.edu.nwafu.nexus.infrastructure.model.entity.File file = fileMapper.selectById(userFile.getFileId());
        Previewer previewer = ufopFactory.getPreviewer(file.getStorageType());
        if (previewer == null) {
            log.error("预览失败，文件存储类型不支持预览，存储类型: {}", file.getStorageType());
            throw new UploadException("预览失败");
        }
        PreviewFile previewFile = new PreviewFile();
        previewFile.setFileUrl(file.getUrl());
        try {
            if ("true".equals(previewDto.getIsMin())) {
                previewer.imageThumbnailPreview(httpServletResponse, previewFile);
            } else {
                previewer.imageOriginalPreview(httpServletResponse, previewFile);
            }
        } catch (Exception e) {
            //org.apache.catalina.connector.ClientAbortException: java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
            if (!e.getMessage().contains("ClientAbortException")) {
                log.error("预览文件出现异常：{}", e.getMessage());
            }
        }

    }

    @Override
    public void previewPictureFile(HttpServletResponse httpServletResponse, PreviewDto previewDto) {
        byte[] bytesUrl = Base64.getDecoder().decode(previewDto.getUrl());
        PictureFile pictureFile = new PictureFile();
        pictureFile.setUrl(new String(bytesUrl));
        pictureFile = pictureFileMapper.selectOne(new QueryWrapper<>(pictureFile));
        Previewer previewer = ufopFactory.getPreviewer(pictureFile.getStorageType());
        if (previewer == null) {
            log.error("预览失败，文件存储类型不支持预览，storageType:{}", pictureFile.getStorageType());
            throw new UploadException("预览失败");
        }
        PreviewFile previewFile = new PreviewFile();
        previewFile.setFileUrl(pictureFile.getUrl());
        try {
            String mime = MimeUtils.getMime(pictureFile.getExtension());
            httpServletResponse.setHeader("Content-Type", mime);

            String fileName = pictureFile.getFileName() + "." + pictureFile.getExtension();
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

            httpServletResponse.addHeader("Content-Disposition", "fileName=" + fileName);// 设置文件名

            previewer.imageOriginalPreview(httpServletResponse, previewFile);
        } catch (Exception e) {
            //org.apache.catalina.connector.ClientAbortException: java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
            if (!e.getMessage().contains("ClientAbortException")) {
                log.error("预览文件出现异常：{}", e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteFile(cn.edu.nwafu.nexus.infrastructure.model.entity.File file) {
        Deleter deleter = ufopFactory.getDeleter(file.getStorageType());
        DeleteFile deleteFile = new DeleteFile();
        deleteFile.setFileUrl(file.getUrl());
        deleter.delete(deleteFile);
    }

    @Override
    public Long selectStorageSizeByUserId(String userId) {
        return userFileMapper.selectStorageSizeByUserId(userId);
    }

    @Override
    public void handleFileDownload(HttpServletRequest request, HttpServletResponse response, DownloadFileDto downloadFileDto) {
        String token = extractToken(request);

        boolean authResult = fileHandler.checkAuthDownloadAndPreview(
                downloadFileDto.getShareBatchNum(),
                downloadFileDto.getExtractionCode(),
                token,
                Collections.singletonList(downloadFileDto.getId()),
                null
        );
        if (!authResult) {
            log.error("没有权限下载");
            return;
        }
        UserFile userFile = userFileService.getById(downloadFileDto.getId());
        String fileName = generateFileName(userFile);
        // 设置响应头
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        downloadFile(response, downloadFileDto);
    }

    @Override
    public void handleBatchFileDownload(HttpServletRequest request, HttpServletResponse response,
                                        BatchDownloadFileDto batchDownloadFileDto) {
        String token = extractToken(request);

        boolean authResult = fileHandler.checkAuthDownloadAndPreview(
                batchDownloadFileDto.getShareBatchNum(),
                batchDownloadFileDto.getExtractionCode(),
                token,
                batchDownloadFileDto.getIds(),
                null
        );
        if (!authResult) {
            Asserts.fail("没有权限下载");
            return;
        }

        List<String> userFileIds = collectUserFileIds(batchDownloadFileDto.getIds());
        UserFile userFile = userFileService.getById(batchDownloadFileDto.getIds().get(0));

        // 设置响应头
        response.setContentType("application/force-download");
        String fileName = String.valueOf(System.currentTimeMillis());
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + ".zip");

        downloadUserFileList(response, userFile.getFilePath(), fileName, userFileIds);
    }

    @Override
    public void handleFilePreview(HttpServletRequest request, HttpServletResponse response, PreviewDto previewDto)
            throws IOException {
        if (previewDto.getPlatform() != null && previewDto.getPlatform() == 2) {
            previewPictureFile(response, previewDto);
            return;
        }

        String token = previewDto.getToken();
        if (StrUtil.isEmpty(token)) {
            token = extractToken(request);
        }

        UserFile userFile = userFileService.getById(previewDto.getId());
        boolean authResult = fileHandler.checkAuthDownloadAndPreview(
                previewDto.getShareBatchNum(),
                previewDto.getExtractionCode(),
                token,
                Collections.singletonList(previewDto.getId()),
                previewDto.getPlatform()
        );

        if (!authResult) {
            log.error("没有权限预览");
            return;
        }

        handlePreviewResponse(request, response, userFile);
    }

    @Override
    public String isFileUploadComplete(String identifier) {
        // 1. 检查上传任务状态
        LambdaQueryWrapper<UploadTask> taskWrapper = new LambdaQueryWrapper<>();
        taskWrapper.eq(UploadTask::getIdentifier, identifier);
        UploadTask uploadTask = uploadTaskMapper.selectOne(taskWrapper);

        if (uploadTask != null && UploadFileStatusEnum.SUCCESS.getCode().equals(uploadTask.getUploadStatus())) {
            return uploadTask.getFileId();
        }

        // 2. 如果没有成功的任务，检查是否有对应的文件记录
        LambdaQueryWrapper<File> fileWrapper = new LambdaQueryWrapper<>();
        fileWrapper.eq(File::getIdentifier, identifier);
        File file = fileMapper.selectOne(fileWrapper);

        if (file != null) {
            // 查找对应的userFile
            LambdaQueryWrapper<UserFile> userFileWrapper = new LambdaQueryWrapper<>();
            userFileWrapper.eq(UserFile::getFileId, file.getId())
                    .isNull(UserFile::getDeleteTime)
                    .last("LIMIT 1");
            UserFile userFile = userFileMapper.selectOne(userFileWrapper);
            return userFile != null ? userFile.getFileId() : null;
        }

        return null;
    }

    private String extractToken(HttpServletRequest request) {
        String token = "";
        Cookie[] cookieArr = request.getCookies();
        if (cookieArr != null) {
            for (Cookie cookie : cookieArr) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    private String generateFileName(UserFile userFile) {
        String fileName;
        if (userFile.getIsDir() == 1) {
            fileName = userFile.getFileName() + ".zip";
        } else {
            fileName = userFile.getFileName() + "." + userFile.getExtension();
        }
        return new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }

    private List<String> collectUserFileIds(List<String> ids) {
        List<String> userFileIds = new ArrayList<>();
        for (String userFileId : ids) {
            UserFile userFile = userFileService.getById(userFileId);
            if (userFile.getIsDir() == 0) {
                userFileIds.add(userFileId);
            } else {
                SystemFile systemFile = new SystemFile(userFile.getFilePath(), userFile.getFileName(), true);
                List<UserFile> userFileList = userFileService.selectUserFileByLikeRightFilePath(systemFile.getPath(), userFile.getUserId());
                List<String> subFileIds = userFileList.stream().map(UserFile::getId).toList();
                userFileIds.add(userFile.getId());
                userFileIds.addAll(subFileIds);
            }
        }
        return userFileIds;
    }

    private void handlePreviewResponse(HttpServletRequest request, HttpServletResponse response, UserFile userFile)
            throws IOException {
        String fileName = userFile.getFileName() + "." + userFile.getExtension();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        response.addHeader("Content-Disposition", "fileName=" + fileName);
        String mime = MimeUtils.getMime(userFile.getExtension());
        response.setHeader("Content-Type", mime);

        if (UFOPUtils.isImageFile(userFile.getExtension())) {
            response.setHeader("cache-control", "public");
        }

        File fileBean = fileService.getById(userFile.getFileId());
        if (UFOPUtils.isVideoFile(userFile.getExtension())
                || "mp3".equalsIgnoreCase(userFile.getExtension())
                || "flac".equalsIgnoreCase(userFile.getExtension())) {
            handleMediaPreview(request, response, fileBean);
        } else {
            previewFile(response, PreviewDto.builder().id(userFile.getId()).build());
        }
    }

    private void handleMediaPreview(HttpServletRequest request, HttpServletResponse response, File fileBean)
            throws IOException {
        String rangeString = request.getHeader("Range");
        int start = 0;
        if (StrUtil.isNotBlank(rangeString)) {
            start = Integer.parseInt(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
        }

        Downloader downloader = ufopFactory.getDownloader(fileBean.getStorageType());
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(fileBean.getUrl());

        Range range = new Range();
        range.setStart(start);
        if (start + 1024 * 1024 >= fileBean.getSize().intValue()) {
            range.setLength(fileBean.getSize().intValue() - start);
        } else {
            range.setLength(1024 * 1024);
        }
        downloadFile.setRange(range);

        try (InputStream inputStream = downloader.getInputStream(downloadFile);
             OutputStream outputStream = response.getOutputStream()) {

            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", "bytes " + start + "-" +
                    (fileBean.getSize() - 1) + "/" + fileBean.getSize());

            IOUtils.copy(inputStream, outputStream);

        } finally {
            if (downloadFile.getOssClient() != null) {
                downloadFile.getOssClient().shutdown();
            }
        }
    }
}
