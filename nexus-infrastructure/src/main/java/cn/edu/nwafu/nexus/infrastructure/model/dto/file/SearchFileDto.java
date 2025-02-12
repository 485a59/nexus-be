package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class SearchFileDto {
    @Schema(description = "文件名", required = true)
    private String name;
    @Schema(description = "当前页", required = true)
    private long pageNum;
    @Schema(description = "每页数量", required = true)
    private long pageSize;
}
