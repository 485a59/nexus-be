package cn.edu.nwafu.nexus.common.util;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 工具类。
 *
 * @author Huang Z.Y.
 */
public class SpringUtils implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    /**
     * 通过名称获取 Bean。
     *
     * @param name Bean 名称
     * @return Bean 实例
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过 class 获取 Bean。
     *
     * @param clazz 类实例
     * @param <T>   类的泛型
     * @return 类实例对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过 {@code name} 和 {@code Clazz} 返回指定的 Bean。
     *
     * @param name  Bean 名称
     * @param clazz 类名称
     * @param <T>   类的泛型
     * @return 类实例对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }
}
