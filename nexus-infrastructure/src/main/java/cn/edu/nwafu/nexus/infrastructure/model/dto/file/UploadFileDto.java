package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 上传文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "上传文件 DTO", required = true)
public class UploadFileDto {
    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "切片数量")
    private int chunkNumber;

    @Schema(description = "切片大小")

    private long chunkSize;
    @Schema(description = "相对路径")
    private String relativePath;

    @Schema(description = "所有切片")
    private int totalChunks;
    @Schema(description = "总大小")
    private long totalSize;
    @Schema(description = "当前切片大小")
    private long currentChunkSize;
    @Schema(description = "md5码")
    private String identifier;
}
