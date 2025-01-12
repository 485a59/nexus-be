package cn.edu.nwafu.nexus.common.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;

/**
 * {@link Long} 和 {@code string} 转换器。
 *
 * @author Huang Z.Y.
 */
public enum LongStringConverter implements Converter<Long> {
    /**
     * 实例
     */
    INSTANCE;

    @Override
    public Class<?> supportJavaTypeKey() {
        return Long.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Long convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                  GlobalConfiguration globalConfiguration) throws ParseException {
        return Long.parseLong(cellData.getStringValue());
    }

    @Override
    public WriteCellData<String> convertToExcelData(Long value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(String.valueOf(value));
    }
}
    