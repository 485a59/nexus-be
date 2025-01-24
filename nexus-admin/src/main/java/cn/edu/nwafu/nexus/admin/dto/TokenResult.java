package cn.edu.nwafu.nexus.admin.dto;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class TokenResult {
    private String token;
    private LoginUserResult currentUser;
}
