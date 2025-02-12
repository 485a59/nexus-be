package cn.edu.nwafu.nexus.domain.response;

import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * 上传文件结果。
 *
 * @author Huang Z.Y.
 */
@Data
public class UploadFileResult {
    private String fileName;
    private String extendName;
    private long fileSize;
    private String fileUrl;
    private String identifier;
    private StorageTypeEnum storageType;
    private UploadFileStatusEnum status;
    private BufferedImage bufferedImage;
}
