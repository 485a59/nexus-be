package cn.edu.nwafu.nexus.ufop.operation.preview.support;

import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS 文件预览类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class FastDfsPreviewer extends Previewer {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public FastDfsPreviewer() {
    }

    public FastDfsPreviewer(ThumbImage thumbImage) {
        setThumbImage(thumbImage);
    }

    protected InputStream getInputStream(PreviewFile previewFile) {
        String group = "group1";
        String path = previewFile.getFileUrl().substring(previewFile.getFileUrl().indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
        return new ByteArrayInputStream(bytes);
    }
}
