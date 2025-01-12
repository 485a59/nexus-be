package cn.edu.nwafu.nexus.infrastructure.user.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.SetUtils;

import java.util.Set;

/**
 * @author Huang Z.Y.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDetails {
    public static final RoleDetails EMPTY_ROLE = new RoleDetails();
    public static final long ADMIN_ROLE_ID = -1;
    public static final String ADMIN_ROLE_KEY = "admin";
    public static final String ALL_PERMISSIONS = "*:*:*";

    public static final Set<String> ADMIN_PERMISSIONS = SetUtils.hashSet(ALL_PERMISSIONS);
    private Long roleId;
    private String roleName;
    private DataScopeEnums dataScope;
    private Set<Long> deptIdSet;
    private String roleKey;
    private Set<String> menuPermissions;
    private Set<Long> menuIds;

    public RoleDetails(Long roleId, String roleKey, DataScopeEnums dataScope, Set<Long> deptIdSet,
                       Set<String> menuPermissions, Set<Long> menuIds) {
        this.roleId = roleId;
        this.roleKey = roleKey;
        this.dataScope = dataScope;
        this.deptIdSet = deptIdSet;
        this.menuPermissions = menuPermissions != null ? menuPermissions : SetUtils.emptySet();
        this.menuIds = menuIds != null ? menuIds : SetUtils.emptySet();
    }
}
