package cn.edu.nwafu.nexus.ufop.exception.operation;

/**
 * 下载操作异常类。
 *
 * @author Huang Z.Y.
 */
public class DownloadException extends RuntimeException {
    public DownloadException(Throwable cause) {
        super("下载出现异常", cause);
    }

    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
