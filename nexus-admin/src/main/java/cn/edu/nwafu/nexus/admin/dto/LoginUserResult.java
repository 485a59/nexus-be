package cn.edu.nwafu.nexus.admin.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Huang Z.Y.
 */
@Data
public class LoginUserResult {
    private UserResult userInfo;
    private String roleKey;
    private Set<String> permissions;
}
