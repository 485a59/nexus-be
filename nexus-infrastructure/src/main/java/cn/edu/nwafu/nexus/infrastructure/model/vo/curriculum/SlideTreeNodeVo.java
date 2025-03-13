package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Data
public class SlideTreeNodeVo {
    private String id;
    private String name;
    private String url;
    private String author;
    private String description;
    private List<SlideTreeNodeVo> children;
}
