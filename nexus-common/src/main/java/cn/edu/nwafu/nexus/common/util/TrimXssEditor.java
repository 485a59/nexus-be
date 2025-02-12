package cn.edu.nwafu.nexus.common.util;

/**
 * @author Huang Z.Y.
 */

import cn.hutool.http.HtmlUtil;
import cn.hutool.poi.excel.cell.CellEditor;
import org.apache.poi.ss.usermodel.Cell;

public class TrimXssEditor implements CellEditor {
    @Override
    public Object edit(Cell cell, Object value) {
        if (value instanceof String) {
            return HtmlUtil.cleanHtmlTag(value.toString());
        }
        return value;
    }
}