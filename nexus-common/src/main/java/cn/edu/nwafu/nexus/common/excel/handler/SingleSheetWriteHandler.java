package cn.edu.nwafu.nexus.common.excel.handler;

import cn.edu.nwafu.nexus.common.excel.annotation.ResponseExcel;
import cn.edu.nwafu.nexus.common.excel.exception.ExcelException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理单页工作表。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {
    public SingleSheetWriteHandler() {
    }

    /**
     * 仅当 obj 是一个 List 且列表不为空并且列表中的元素不是 List 时返回 true。
     *
     * @param obj 要判断的对象 返回对象
     * @return boolean
     */
    @Override
    public boolean support(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && !(objList.get(0) instanceof List);
        } else {
            throw new ExcelException("@ResponseExcel 的返回值必须是 List 类型。");
        }
    }


    @Override
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> eleList = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

        WriteSheet sheet;
        if (CollectionUtils.isEmpty(eleList)) {
            sheet = EasyExcel.writerSheet(responseExcel.sheets()[0].sheetName()).build();
        } else {
            // 如果存在模板，未指定工作表名称
            Class<?> dataClass = eleList.get(0).getClass();
            sheet = this.sheet(dataClass);
        }

        // 写 Sheet
        excelWriter.write(eleList, sheet);

        excelWriter.finish();
    }
}