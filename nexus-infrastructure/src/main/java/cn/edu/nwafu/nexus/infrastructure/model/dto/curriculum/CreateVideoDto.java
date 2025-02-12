package cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum;

import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class CreateVideoDto extends UploadFileDto {
    private String name;
    private String fileId;
    private Integer chapterId;
    private String lecturer;
    private Integer views;
    private String poster;
    private String description;
}
