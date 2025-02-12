package cn.edu.nwafu.nexus.infrastructure.model.dto.system;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class OperationLogDto {
    private String businessType;
    private String status;
    private String username;
    private String requestModule;
}
