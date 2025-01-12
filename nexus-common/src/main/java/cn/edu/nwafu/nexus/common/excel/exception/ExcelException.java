package cn.edu.nwafu.nexus.common.excel.exception;

import java.io.Serial;

/**
 * Excel 自定义异常类。
 *
 * @author Huang Z.Y.
 */
public class ExcelException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 232532613925L;

    public ExcelException(String message) {
        super(message);
    }
}
