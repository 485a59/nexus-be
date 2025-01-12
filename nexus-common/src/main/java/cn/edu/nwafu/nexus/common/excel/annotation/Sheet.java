package cn.edu.nwafu.nexus.common.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 工作簿注解。
 *
 * @author Huang Z.Y.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {
    /**
     * Sheet 编号
     */
    int sheetNo() default -1;

    /**
     * Sheet 名
     */
    String sheetName();
}
