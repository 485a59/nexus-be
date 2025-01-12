package cn.edu.nwafu.nexus.common.excel.aop;

import cn.edu.nwafu.nexus.common.excel.annotation.ResponseExcel;
import cn.edu.nwafu.nexus.common.excel.processor.NameProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 切面类，用于在使用 {@link cn.edu.nwafu.nexus.common.excel.annotation.ResponseExcel @ResponseExcel}
 * 注解的方法执行前确定 Excel 文件名。
 *
 * @author Huang Z.Y.
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {
    /**
     * 用于在请求属性中存储 Excel 名称的常量键
     */
    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";
    /**
     * 根据 SpEL 表达式确定 Excel 文件名的处理器
     */
    private final NameProcessor processor;

    /**
     * 此方法在任何带有 @ResponseExcel 注解的方法执行之前执行。
     * 它根据注解的值或当前时间戳确定要使用的 Excel 文件名，
     * 并将确定的名称存储在请求属性中以便后续使用。
     *
     * @param point 表示方法执行的连接点
     * @param excel 方法上的 ResponseExcel 注解
     */
    @Before("@annotation(excel)")
    public void around(JoinPoint point, ResponseExcel excel) {
        // 从连接点获取方法签名
        MethodSignature ms = (MethodSignature) point.getSignature();
        // 获取 ResponseExcel 注解中指定的名称
        String name = excel.name();
        // 如果未指定名称，则使用当前时间戳作为名称
        if (!StringUtils.hasText(name)) {
            name = LocalDateTime.now().toString();
        } else {
            // 否则，使用 NameProcessor 根据 SPEL 表达式确定名称
            name = processor.doDetermineName(point.getArgs(), ms.getMethod(), excel.name());
        }

        // 获取当前请求的属性，并将确定的名称存储在请求范围中
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, RequestAttributes.SCOPE_REQUEST);
    }
}
    