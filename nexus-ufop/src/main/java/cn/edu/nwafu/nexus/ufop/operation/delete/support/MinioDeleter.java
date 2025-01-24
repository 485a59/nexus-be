package cn.edu.nwafu.nexus.ufop.operation.delete.support;

import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.exception.operation.DeleteException;
import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO 删除文件实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class MinioDeleter extends Deleter {
    private MinioConfig minioConfig;

    public MinioDeleter() {
    }

    public MinioDeleter(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void delete(DeleteFile deleteFile) {
        try {
            MinioClient minioClient =
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            // 从 bucket 中删除
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucketName()).object(deleteFile.getFileUrl()).build());
            log.info("successfully removed mybucket/myobject");
        } catch (MinioException e) {
            log.error("Error: " + e);
            throw new DeleteException("Minio删除文件失败", e);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new DeleteException("Minio删除文件失败", e);
        }
        deleteCacheFile(deleteFile);
    }
}
