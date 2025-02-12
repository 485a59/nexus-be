package cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum;

import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateTextbookDto extends UploadFileDto {
    private String name;
    private String fileId;
    private String publisher;
    private Date publishDate;
    private String isbn;
    private String author;
    private String edition;
    private String description;
}
