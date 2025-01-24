package cn.edu.nwafu.nexus.ufop.operation.upload.request;

import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 上传多文件。
 *
 * @author Huang Z.Y.
 */
@Getter
public class UploadMultipartFile {
    MultipartFile multipartFile = null;

    private UploadMultipartFile() {
    }

    public UploadMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getFileName() {
        String originalName = getMultipartFile().getOriginalFilename();
        if (!originalName.contains(".")) {
            return originalName;
        }
        return originalName.substring(0, originalName.lastIndexOf("."));
    }

    public String getExtension() {
        String originalName = getMultipartFile().getOriginalFilename();
        return FilenameUtils.getExtension(originalName);
    }

    public String getFileUrl() {
        String uuid = UUID.randomUUID().toString();
        return UFOPUtils.getUploadFileUrl(uuid, getExtension());
    }

    public String getFileUrl(String identify) {
        return UFOPUtils.getUploadFileUrl(identify, getExtension());
    }

    public InputStream getUploadInputStream() throws IOException {
        return getMultipartFile().getInputStream();
    }

    public byte[] getUploadBytes() throws IOException {
        return getMultipartFile().getBytes();
    }

    public long getSize() {
        return getMultipartFile().getSize();
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }
}
