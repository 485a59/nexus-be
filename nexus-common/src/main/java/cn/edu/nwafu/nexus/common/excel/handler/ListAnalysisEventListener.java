package cn.edu.nwafu.nexus.common.excel.handler;

import cn.edu.nwafu.nexus.common.excel.vo.ErrorMessage;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.List;

/**
 * 提供一个事件监听器，将 Excel 数据解析为对象列表。
 *
 * @author Huang Z.Y.
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {
    /**
     * 获取 Excel 解析得到的对象列表。
     *
     * @return 集合
     */
    public abstract List<T> getList();

    /**
     * 获取异常验证结果。
     *
     * @return 集合
     */
    public abstract List<ErrorMessage> getErrors();
}