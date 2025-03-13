package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Data
public class SoftwareTreeNodeVo {
    private String id;
    private String name;
    private String url;
    private String version;
    private String platform;
    private Long size;
    private Date updateTime;
    private String description;
    private List<SoftwareTreeNodeVo> children;
}
