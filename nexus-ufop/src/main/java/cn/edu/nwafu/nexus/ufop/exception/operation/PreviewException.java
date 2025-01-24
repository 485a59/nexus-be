package cn.edu.nwafu.nexus.ufop.exception.operation;

import cn.edu.nwafu.nexus.ufop.exception.UFOPException;

/**
 * 预览操作异常类。
 *
 * @author Huang Z.Y.
 */
public class PreviewException extends UFOPException {
    public PreviewException(Throwable cause) {
        super("预览出现异常", cause);
    }

    public PreviewException(String message) {
        super(message);
    }

    public PreviewException(String message, Throwable cause) {
        super(message, cause);
    }
}
