package cn.edu.nwafu.nexus.ufop.operation.copy.support;

import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * FastDFS 文件上传实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class FastDfsCopier extends Copier {
    @Resource
    AppendFileStorageClient defaultAppendFileStorageClient;

    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        StorePath storePath = new StorePath();
        try {
            storePath = defaultAppendFileStorageClient.uploadAppenderFile("group1", inputStream,
                    inputStream.available(), copyFile.getExtension());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return storePath.getPath();
    }
}
