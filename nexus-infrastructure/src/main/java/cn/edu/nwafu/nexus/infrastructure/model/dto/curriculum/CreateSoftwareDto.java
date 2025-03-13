package cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum;

import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class CreateSoftwareDto extends UploadFileDto {
    private String name;
    private String fileId;
    private String version;
    private String platform;
    private String description;
    private String category;
}
