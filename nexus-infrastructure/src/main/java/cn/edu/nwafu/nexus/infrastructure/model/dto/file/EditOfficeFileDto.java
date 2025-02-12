package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 编辑 Office 文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "编辑 Office 文件 DTO", required = true)
public class EditOfficeFileDto {
    private String userFileId;
}
