package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class SlideListVo {
    private String id;
    private String name;
    private String fileId;
    private Integer chapterId;
    private String chapterName;
    private String author;
    private String description;
    private String url;
}
