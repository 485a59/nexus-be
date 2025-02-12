package cn.edu.nwafu.nexus.infrastructure.model.dto.system;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author Huang Z.Y.
 */
@Data
public class UpdateRoleDto extends CreateRoleDto {
    @NotNull
    @PositiveOrZero
    private Long roleId;
}
