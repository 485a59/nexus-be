package cn.edu.nwafu.nexus.common.constant;

import lombok.Getter;

/**
 * @author Huang Z.Y.
 */
@Getter
public enum ResourceTypeEnums {
    TEXTBOOK(1, "教材资源"),
    VIDEO(2, "视频资源"),
    SLIDE(3, "PPT资源"),
    SOFTWARE(4, "软件资源"),
    DOCUMENT(5, "文档资源");

    private final Integer code;
    private final String description;

    ResourceTypeEnums(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
