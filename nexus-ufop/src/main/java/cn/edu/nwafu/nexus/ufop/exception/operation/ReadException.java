package cn.edu.nwafu.nexus.ufop.exception.operation;

import cn.edu.nwafu.nexus.ufop.exception.UFOPException;

/**
 * 文件读取操作异常类。
 *
 * @author Huang Z.Y.
 */
public class ReadException extends UFOPException {
    public ReadException(Throwable cause) {
        super("文件读取出现异常", cause);
    }

    public ReadException(String message) {
        super(message);
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
