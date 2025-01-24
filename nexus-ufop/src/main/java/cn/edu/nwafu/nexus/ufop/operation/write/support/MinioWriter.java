package cn.edu.nwafu.nexus.ufop.operation.write.support;

import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.operation.write.Writer;
import cn.edu.nwafu.nexus.ufop.operation.write.domain.WriteFile;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO 文件写入实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class MinioWriter extends Writer {
    private MinioConfig minioConfig;

    public MinioWriter() {
    }

    public MinioWriter(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        try {
            MinioClient minioClient =
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(UFOPUtils.getAliyunObjectNameByFileUrl(writeFile.getFileUrl())).stream(
                                    inputStream, inputStream.available(), -1)
                            .build());

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}

