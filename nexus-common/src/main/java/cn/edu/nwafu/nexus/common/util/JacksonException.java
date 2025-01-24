package cn.edu.nwafu.nexus.common.util;

/**
 * @author Huang Z.Y.
 */
public class JacksonException extends RuntimeException {
    public JacksonException(String message, Exception e) {
        super(message, e);
    }
}
