package cn.edu.nwafu.nexus.common.excel.annotation;

import com.alibaba.excel.support.ExcelTypeEnum;

import java.lang.annotation.*;

/**
 * 导出 Excel 注解。
 *
 * @author Huang Z.Y.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {
    /**
     * 文件名
     */
    String name() default "";

    /**
     * 文件类型 (xlsx xls)
     */
    ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;

    /**
     * Sheet 名, 可能有多个
     *
     * @return String[]
     */
    Sheet[] sheets() default @Sheet(sheetName = "sheet1");

    /**
     * 内存操作，减少耗时
     */
    boolean inMemory() default false;
}
