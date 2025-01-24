package cn.edu.nwafu.nexus.ufop.constant;

import lombok.Getter;

/**
 * 文件权限枚举类。
 *
 * @author Huang Z.Y.
 */
@Getter
public enum FilePermissionEnum {
    NO("0", "无权限"),
    READ("1", "读取"),
    READ_WRITE("2", "读取/写入"),
    OWNER("3", "所有者");

    private final String type;
    private final String desc;

    FilePermissionEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
