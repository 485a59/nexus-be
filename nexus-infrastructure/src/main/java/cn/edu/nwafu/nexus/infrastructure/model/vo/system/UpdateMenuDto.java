package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateMenuDto;

import javax.validation.constraints.NotNull;

/**
 * @author Huang Z.Y.
 */
public class UpdateMenuDto extends CreateMenuDto {
    @NotNull
    private Integer menuId;
}
