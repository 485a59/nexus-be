package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建 Office 文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "创建 Office 文件 DTO", required = true)
public class CreateOfficeFileDto {
    private String path;
    private String name;
    private String extension;
}
