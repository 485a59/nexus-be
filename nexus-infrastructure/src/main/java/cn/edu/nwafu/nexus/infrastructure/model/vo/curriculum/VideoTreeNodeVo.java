package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Data
public class VideoTreeNodeVo {
    private String id;
    private String name;
    private String url;
    private String duration;
    private String lecturer;
    private String description;
    private Integer views;
    private List<VideoTreeNodeVo> children;
}
