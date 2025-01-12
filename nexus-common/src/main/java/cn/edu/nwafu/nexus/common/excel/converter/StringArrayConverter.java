package cn.edu.nwafu.nexus.common.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 字符串数组和 {@code string} 的转换。
 *
 * @author Huang Z.Y.
 */
public enum StringArrayConverter implements Converter<String[]> {
    /**
     * 实例
     */
    INSTANCE;

    @Override
    public Class<?> supportJavaTypeKey() {
        return String[].class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String[] convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                      GlobalConfiguration globalConfiguration) throws ParseException {
        return cellData.getStringValue().split(",");
    }

    @Override
    public WriteCellData<String> convertToExcelData(String[] value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(Arrays.stream(value).collect(Collectors.joining()));
    }
}
    