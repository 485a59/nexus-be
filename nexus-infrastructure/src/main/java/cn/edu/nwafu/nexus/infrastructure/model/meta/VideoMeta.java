package cn.edu.nwafu.nexus.infrastructure.model.meta;

import lombok.Data;

/**
 * 视频元数据。
 */
@Data
public class VideoMeta {
    /**
     * 讲师
     */
    private String lecturer;
    /**
     * 观看次数
     */
    private Integer views;
    /**
     * 封面图URL
     */
    private String poster;
} 