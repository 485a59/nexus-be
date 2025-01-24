package cn.edu.nwafu.nexus.ufop.operation.read.support;

import cn.edu.nwafu.nexus.ufop.exception.operation.ReadException;
import cn.edu.nwafu.nexus.ufop.operation.read.Reader;
import cn.edu.nwafu.nexus.ufop.operation.read.domain.ReadFile;
import cn.edu.nwafu.nexus.ufop.util.ReadFileUtils;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class FastDfsReader extends Reader {
    @Resource
    private FastFileStorageClient fastFileStorageClient;

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

    public InputStream getInputStream(String fileUrl) {
        String group;
        group = "group1";
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
        return new ByteArrayInputStream(bytes);
    }
}

