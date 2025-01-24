package cn.edu.nwafu.nexus.ufop.operation.preview.support;

import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO 文件预览实现类。
 *
 * @author Huang Z.Y.
 */
@Setter
@Getter
@Slf4j
public class MinioPreviewer extends Previewer {
    private MinioConfig minioConfig;

    public MinioPreviewer() {
    }

    public MinioPreviewer(MinioConfig minioConfig, ThumbImage thumbImage) {
        setMinioConfig(minioConfig);
        setThumbImage(thumbImage);
    }

    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        InputStream inputStream = null;
        try {
            MinioClient minioClient =
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(previewFile.getFileUrl()).build());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return inputStream;
    }
}
