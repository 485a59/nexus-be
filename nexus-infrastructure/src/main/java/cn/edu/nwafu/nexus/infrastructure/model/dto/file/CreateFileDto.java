package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import cn.edu.nwafu.nexus.common.constant.RegexConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Huang Z.Y.
 */
@Data
public class CreateFileDto {
    @Schema(description = "文件路径", required = true)
    private String path;

    @Schema(description = "文件名", required = true)
    @NotBlank(message = "文件名不能为空")
    @Pattern(regexp = RegexConstants.FILE_NAME_REGEX, message = "文件名不合法", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String name;

    @Schema(description = "扩展名", required = true)
    private String extension;
}
