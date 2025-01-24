package cn.edu.nwafu.nexus.common.api;

/**
 * API 返回码接口。
 *
 * @author Huang Z.Y.
 */
public interface IErrorCode {
    /**
     * 返回错误码。
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 返回具体的详细错误描述。
     *
     * @return 错误描述
     */
    String getMessage();
}
