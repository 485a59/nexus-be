package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
public class MenuListVo {
    private Integer id;
    private Integer parentId;
    private String name;
    private String routerName;
    private String path;
    private Integer rank;
    private Integer menuType;
    private String menuTypeStr;
    private Boolean isButton;
    private Integer status;
    private Date createTime;
    private String icon;
}
