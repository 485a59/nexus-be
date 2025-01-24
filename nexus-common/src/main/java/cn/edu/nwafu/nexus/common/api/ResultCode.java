package cn.edu.nwafu.nexus.common.api;

/**
 * 错误码合集。
 *
 * @author Huang Z.Y.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或 token 已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private final int code;
    private final String message;

    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}