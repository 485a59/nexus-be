package cn.edu.nwafu.nexus.common.excel.handler;

import cn.edu.nwafu.nexus.common.excel.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.excel.processor.Validators;
import cn.edu.nwafu.nexus.common.excel.vo.ErrorMessage;
import com.alibaba.excel.context.AnalysisContext;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的 {@link ListAnalysisEventListener}。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {
    /**
     * 存储解析后的 Excel 数据对象列表
     */
    private final List<Object> list = new ArrayList<>();
    /**
     * 存储错误信息列表
     */
    private final List<ErrorMessage> errorMessageList = new ArrayList<>();
    /**
     * 行号
     */
    private Long lineNum = 1L;

    /**
     * 在分析过程中，针对从 Excel 文件解析出的每一行数据所调用的回调方法。
     *
     * @param o               表示 Excel 一行数据的解析数据对象
     * @param analysisContext 分析上下文
     */
    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        // 行号加一
        lineNum++;
        // 验证解析的数据对象
        Set<ConstraintViolation<Object>> violations = Validators.validate(o);
        if (!violations.isEmpty()) {
            // 验证失败
            Set<String> messageSet = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            errorMessageList.add(new ErrorMessage(lineNum, messageSet));
        } else {
            // 为带有 @ExcelLine 注解且类型为 Long 的字段设置行号值
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 为行号字段赋值
                if (field.isAnnotationPresent(ExcelColumn.class) && field.getType() == Long.class) {
                    try {
                        field.setAccessible(true);
                        field.set(o, lineNum);
                    } catch (IllegalAccessException e) {
                        // 忽略
                    }
                }
            }
            list.add(o);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 记录日志：Excel 读取分析完成
        log.debug("Excel 读取分析完成");
    }

    @Override
    public List<Object> getList() {
        return list;
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return errorMessageList;
    }
}