package cn.edu.nwafu.nexus.common.excel.handler;

import cn.edu.nwafu.nexus.common.excel.annotation.ResponseExcel;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 定义将数据导出到 Excel 文件的处理器。
 *
 * @author Huang Z.Y.
 */
public interface SheetWriteHandler {
    /**
     * 是否支持。
     *
     * @param obj 要判断的对象
     * @return 是否支持
     */
    boolean support(Object obj);

    /**
     * 将数据导出为 Excel 文件。
     *
     * @param o             对象
     * @param response      输出对象
     * @param responseExcel 注解
     */
    @SneakyThrows(UnsupportedEncodingException.class)
    void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

    /**
     * 将对象写入 Excel 文件。
     *
     * @param o             对象
     * @param response      输出对象
     * @param responseExcel 注解
     */
    void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);
}