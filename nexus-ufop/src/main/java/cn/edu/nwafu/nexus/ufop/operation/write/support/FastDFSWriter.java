package cn.edu.nwafu.nexus.ufop.operation.write.support;

import cn.edu.nwafu.nexus.ufop.operation.write.Writer;
import cn.edu.nwafu.nexus.ufop.operation.write.domain.WriteFile;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * FastDFS 文件写入实现类。
 *
 * @author Huang Z.Y.
 */
@Component
@Slf4j
public class FastDFSWriter extends Writer {
    @Resource
    AppendFileStorageClient defaultAppendFileStorageClient;

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        defaultAppendFileStorageClient.modifyFile("group1", writeFile.getFileUrl(), inputStream,
                writeFile.getFileSize(), 0);
    }
}
