package cn.edu.nwafu.nexus.ufop.operation.upload.support;

import cn.edu.nwafu.nexus.common.service.RedisService;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import cn.edu.nwafu.nexus.ufop.exception.operation.UploadException;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
import cn.edu.nwafu.nexus.ufop.operation.upload.request.UploadMultipartFile;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MiniIO 文件上传实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class MinioUploader extends Uploader {
    @Resource
    RedisService redisService;
    private MinioConfig minioConfig;

    public MinioUploader() {
    }

    public MinioUploader(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
        // Minio 取消上传逻辑（如果需要）
    }

    @Override
    protected void doUploadFileChunk(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        // Minio 分片上传逻辑（如果需要）
    }

    @Override
    protected UploadFileResult organizationalResults(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        // Minio 组织结果逻辑（如果需要）
        return null;
    }

    @Override
    protected UploadFileResult doUploadFlow(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            uploadMultipartFile.getFileUrl(uploadFile.getIdentifier());
            String fileUrl = UFOPUtils.getUploadFileUrl(uploadFile.getIdentifier(), uploadMultipartFile.getExtension());

            File tempFile = UFOPUtils.getTempFile(fileUrl);
            File processFile = UFOPUtils.getProcessFile(fileUrl);

            byte[] fileData = uploadMultipartFile.getUploadBytes();

            writeByteDataToFile(fileData, tempFile, uploadFile);

            // 判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, processFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(uploadMultipartFile.getFileName());
            uploadFileResult.setExtension(uploadMultipartFile.getExtension());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnum.MINIO);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(uploadMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {
                minioUpload(fileUrl, tempFile, uploadFile);
                uploadFileResult.setFileUrl(fileUrl);
                tempFile.delete();

                if (UFOPUtils.isImageFile(uploadFileResult.getExtension())) {
                    InputStream inputStream = null;
                    try {
                        MinioClient minioClient = MinioClient.builder()
                                .endpoint(minioConfig.getEndpoint())
                                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                                .build();

                        inputStream = minioClient.getObject(GetObjectArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .object(uploadFileResult.getFileUrl())
                                .build());

                        BufferedImage src = ImageIO.read(inputStream);
                        uploadFileResult.setBufferedImage(src);
                    } catch (Exception e) {
                        log.error("读取Minio文件失败", e);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }
                }

                uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
            } else {
                uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLETED);
            }
        } catch (IOException e) {
            throw new UploadException(e);
        }

        return uploadFileResult;
    }

    private void minioUpload(String fileUrl, File file, UploadFile uploadFile) {
        InputStream inputStream = null;
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioConfig.getEndpoint())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                    .build();

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build());
            }

            inputStream = new FileInputStream(file);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileUrl)
                    .stream(inputStream, uploadFile.getTotalSize(), 1024 * 1024 * 5)
                    .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Minio 文件上传失败", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
