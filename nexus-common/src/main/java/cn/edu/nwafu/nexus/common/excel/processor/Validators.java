package cn.edu.nwafu.nexus.common.excel.processor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 检查工具，使用单例模式。
 *
 * @author Huang Z.Y.
 */
public class Validators {
    private static final Validator VALIDATOR;

    static {
        // 构建默认的验证器工厂
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        // 从工厂获取验证器
        VALIDATOR = factory.getValidator();
    }

    // 不允许创建实例
    private Validators() {
    }

    /**
     * 验证 {@code object} 上的所有约束。
     *
     * @param object 要验证的对象
     * @param <T>    要验证的对象的类型
     * @return 约束违规集合，如果没有违规则为空集
     * @throws IllegalArgumentException 如果对象为 {@code null} 或者将 {@code null} 传递给可变参数组
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object) {
        return VALIDATOR.validate(object);
    }
}