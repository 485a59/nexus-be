package cn.edu.nwafu.nexus.ufop.operation.copy.support;

import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * MinIO 文件上传实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class MinioCopier extends Copier {
    private MinioConfig minioConfig;

    public MinioCopier() {
    }

    public MinioCopier(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = UFOPUtils.getUploadFileUrl(uuid, copyFile.getExtension());

        try {
            MinioClient minioClient = createMinioClient();
            ensureBucketExists(minioClient);
            uploadFile(minioClient, inputStream, fileUrl);
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return fileUrl;
    }

    private MinioClient createMinioClient() throws InvalidKeyException, NoSuchAlgorithmException {
        return MinioClient.builder()
                .endpoint(minioConfig.getEndpoint())
                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                .build();
    }

    private void ensureBucketExists(MinioClient minioClient) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
        }
    }

    private void uploadFile(MinioClient minioClient, InputStream inputStream, String fileUrl) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileUrl)
                .stream(inputStream, inputStream.available(), 1024 * 1024 * 5)
                .build());
    }
}
