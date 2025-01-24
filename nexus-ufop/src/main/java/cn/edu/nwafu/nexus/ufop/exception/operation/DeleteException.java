package cn.edu.nwafu.nexus.ufop.exception.operation;

import cn.edu.nwafu.nexus.ufop.exception.UFOPException;

/**
 * 删除操作异常类。
 *
 * @author Huang Z.Y.
 */
public class DeleteException extends UFOPException {
    public DeleteException(Throwable cause) {
        super("删除出现异常", cause);
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
