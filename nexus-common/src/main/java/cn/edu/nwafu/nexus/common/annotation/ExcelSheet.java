package cn.edu.nwafu.nexus.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出 Excel 数据注解。
 *
 * @author Huang Z.Y.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelSheet {
    /**
     * sheet 名称
     */
    String name() default "";
}

