package cn.edu.nwafu.nexus.ufop.exception.operation;

import cn.edu.nwafu.nexus.ufop.exception.UFOPException;

/**
 * 文件写入操作异常类。
 *
 * @author Huang Z.Y.
 */
public class WriteException extends UFOPException {
    public WriteException(Throwable cause) {
        super("文件写入出现异常", cause);
    }

    public WriteException(String message) {
        super(message);
    }

    public WriteException(String message, Throwable cause) {
        super(message, cause);
    }
}