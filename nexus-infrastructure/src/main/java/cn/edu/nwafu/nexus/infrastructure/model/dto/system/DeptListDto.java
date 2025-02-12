package cn.edu.nwafu.nexus.infrastructure.model.dto.system;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class DeptListDto {
    private String id;
    private String parentId;
    private String name;
    private Integer status;
}
