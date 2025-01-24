package cn.edu.nwafu.nexus.ufop.operation.read.support;

import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.exception.operation.ReadException;
import cn.edu.nwafu.nexus.ufop.operation.read.Reader;
import cn.edu.nwafu.nexus.ufop.operation.read.domain.ReadFile;
import cn.edu.nwafu.nexus.ufop.util.ReadFileUtils;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO 文件读取实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class MinioReader extends Reader {
    private MinioConfig minioConfig;

    public MinioReader() {
    }

    public MinioReader(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public String read(ReadFile readFile) {
        String fileUrl = readFile.getFileUrl();
        String fileType = FilenameUtils.getExtension(fileUrl);
        try {
            return ReadFileUtils.getContentByInputStream(fileType, getInputStream(readFile.getFileUrl()));
        } catch (IOException e) {
            throw new ReadException("读取文件失败", e);
        }
    }

    protected InputStream getInputStream(String fileUrl) {
        InputStream inputStream = null;
        try {
            MinioClient minioClient =
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileUrl).build());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return inputStream;
    }
}

