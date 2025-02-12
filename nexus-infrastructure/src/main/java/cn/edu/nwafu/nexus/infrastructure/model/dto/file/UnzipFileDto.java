package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 解压缩文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "解压缩文件 DTO", required = true)
public class UnzipFileDto {
    @Schema(description = "文件 id", required = true)
    private String id;

    @Schema(description = "解压模式 0-解压到当前文件夹， 1-自动创建该文件名目录，并解压到目录里， 2-手动选择解压目录", required = true)
    private int mode;

    @Schema(description = "解压目的文件目录，仅当 unzipMode 为 2 时必传")
    private String path;
}
