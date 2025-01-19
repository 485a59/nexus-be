package cn.edu.nwafu.nexus.domain.constant;

import lombok.Getter;

/**
 * 上传文件状态枚举类。
 *
 * @author Huang Z.Y.
 */
@Getter
public enum UploadFileStatusEnum {
    FAILED(0, "上传失败"),
    SUCCESS(1, "上传成功"),
    UNCOMPLETED(2, "未完成");

    private final int code;
    private final String message;

    UploadFileStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
