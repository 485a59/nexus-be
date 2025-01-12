package cn.edu.nwafu.nexus.common.excel.annotation;

import java.lang.annotation.*;

/**
 * 导入 Excel 注解。
 *
 * @author Huang Z.Y.
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {
    /**
     * 前端文件名，默认为 file
     */
    String fileName() default "file";
}
    