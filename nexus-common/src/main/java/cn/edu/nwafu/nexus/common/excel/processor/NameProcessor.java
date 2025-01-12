package cn.edu.nwafu.nexus.common.excel.processor;

import java.lang.reflect.Method;

/**
 * 解决表头名称。
 *
 * @author Huang Z.Y.
 */
public interface NameProcessor {
    /**
     * 处理表头名称。
     *
     * @param args   拦截参数
     * @param method 方法
     * @param key    表达式
     * @return 解析结果
     */
    String doDetermineName(Object[] args, Method method, String key);
}
    