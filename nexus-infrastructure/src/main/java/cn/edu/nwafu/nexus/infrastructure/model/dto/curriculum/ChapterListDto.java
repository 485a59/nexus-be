package cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class ChapterListDto {
    private String id;
    private String parentId;
    private String name;
    private Integer status;
}
