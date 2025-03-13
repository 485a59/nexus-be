package cn.edu.nwafu.nexus.infrastructure.model.meta;

import lombok.Data;

/**
 * 软件元数据。
 */
@Data
public class SoftwareMeta {
    /**
     * 版本号
     */
    private String version;
    /**
     * 支持平台
     */
    private String platform;
    /**
     * 分类
     */
    private String category;
} 