package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
public class DeptDetailVo {
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer orderNum;

    private String leaderName;

    private String phoneNumber;

    private String email;

    private Integer status;

    private Date createTime;
}
