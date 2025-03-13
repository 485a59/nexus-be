package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class SoftwareListVo {
    private String id;
    private String name;
    private String fileId;
    private String version;
    private String platform;
    private String category;
    private String description;
    private String url;
}
