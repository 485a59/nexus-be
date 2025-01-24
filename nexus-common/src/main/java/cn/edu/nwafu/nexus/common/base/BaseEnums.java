package cn.edu.nwafu.nexus.common.base;

/**
 * 普通的枚举接口。
 *
 * @author Huang Z.Y.
 */
public interface BaseEnums<T> {
    /**
     * 获取枚举的值
     *
     * @return 枚举值
     */
    T getValue();

    /**
     * 获取枚举的描述
     *
     * @return 描述
     */
    String description();
}
