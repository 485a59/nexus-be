package cn.edu.nwafu.nexus.infrastructure.model.dto.system;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author Huang Z.Y.
 */
public class UpdateDeptDto extends CreateDeptDto {
    @NotNull
    @PositiveOrZero
    private Integer deptId;
}
