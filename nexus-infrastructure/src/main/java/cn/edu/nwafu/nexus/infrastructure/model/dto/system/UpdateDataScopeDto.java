package cn.edu.nwafu.nexus.infrastructure.model.dto.system;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
public class UpdateDataScopeDto {
    @NotNull
    @Positive
    private Integer id;
    @NotNull
    @NotEmpty
    private List<Integer> deptIds;
}
