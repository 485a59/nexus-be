package cn.edu.nwafu.nexus.common.excel.annotation;

import java.lang.annotation.*;

/**
 * If need to import excel line numbers for use on entity properties.
 *
 * @author Huang Z.Y.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
}
