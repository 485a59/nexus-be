package cn.edu.nwafu.nexus.domain.enumeration;

/**
 * 文件类型枚举值。
 *
 * @author Huang Z.Y.
 */
public enum FileTypeEnums {
    TOTAL(0, "全部"),
    PICTURE(1, "图片"),
    DOCUMENT(2, "文档"),
    VIDEO(3, "视频"),
    MUSIC(4, "音乐"),
    OTHER(5, "其他"),
    SHARE(6, "分享"),
    RECYCLE(7, "回收站");

    private final int type;
    private final String desc;

    FileTypeEnums(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}

