package cn.edu.nwafu.nexus.common.api;


import lombok.Data;
import lombok.Setter;

/**
 * 通用返回结果封装类。
 *
 * @author Huang Z.Y.
 */
@Data
@Setter
public class CommonResult<T> {
    /**
     * 状态码
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据封装
     */
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果。
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.Http.SUCCESS.code(), ResultCode.Http.SUCCESS.message(), data);
    }

    public static <T> CommonResult<T> success(String message) {
        return CommonResult.success(null, message);
    }

    /**
     * 成功返回结果。
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCode.Http.SUCCESS.code(), message, data);
    }

    /**
     * 失败返回结果。
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.code(), errorCode.message(), null);
    }

    /**
     * 失败返回结果。
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return new CommonResult<T>(errorCode.code(), message, null);
    }

    /**
     * 失败返回结果。
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.Http.FAILED.code(), message, null);
    }

    /**
     * 失败返回结果。
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.Http.FAILED.message());
    }

    /**
     * 参数验证失败返回结果。
     *
     * @param parameters 异常参数字符串
     */
    public static <T> CommonResult<T> validate(String parameters) {
        return failed(ResultCode.Internal.INVALID_PARAMETER, String.format(ResultCode.Internal.INVALID_PARAMETER.message(), parameters));
    }

    /**
     * 未登录返回结果。
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(ResultCode.Http.UNAUTHORIZED.code(), ResultCode.Http.UNAUTHORIZED.message(), data);
    }

    /**
     * 未授权返回结果。
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(ResultCode.Http.FORBIDDEN.code(), ResultCode.Http.FORBIDDEN.message(), data);
    }
}
