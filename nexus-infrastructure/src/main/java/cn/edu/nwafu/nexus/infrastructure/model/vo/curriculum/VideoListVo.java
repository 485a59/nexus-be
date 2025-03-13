package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class VideoListVo {
    private String id;
    private String fileId;
    private String name;
    private Integer chapterId;
    private Integer parentChapterId;
    private String chapterName;
    private String lecturer;
    private String url;
}
