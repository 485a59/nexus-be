package cn.edu.nwafu.nexus.ufop.exception.operation;

import cn.edu.nwafu.nexus.ufop.exception.UFOPException;

/**
 * 创建操作异常类。
 *
 * @author Huang Z.Y.
 */
public class CopyException extends UFOPException {
    public CopyException(Throwable cause) {
        super("创建出现异常", cause);
    }

    public CopyException(String message) {
        super(message);
    }

    public CopyException(String message, Throwable cause) {
        super(message, cause);
    }
}
