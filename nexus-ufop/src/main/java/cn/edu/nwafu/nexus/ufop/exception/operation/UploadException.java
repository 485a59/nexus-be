package cn.edu.nwafu.nexus.ufop.exception.operation;

import cn.edu.nwafu.nexus.ufop.exception.UFOPException;

/**
 * 上传操作异常类。
 *
 * @author Huang Z.Y.
 */
public class UploadException extends UFOPException {
    public UploadException(Throwable cause) {
        super("上传出现异常", cause);
    }

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
