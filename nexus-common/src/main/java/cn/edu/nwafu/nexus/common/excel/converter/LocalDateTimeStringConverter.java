package cn.edu.nwafu.nexus.common.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * {@link LocalDateTime} 和 {@code string} 转换 (yyyy-MM-dd HH:mm:ss or yyyy/MM/dd HH:mm:ss) 。
 *
 * @author Huang Z.Y.
 */
public enum LocalDateTimeStringConverter implements Converter<LocalDateTime> {
    /**
     * 实例
     */
    INSTANCE;

    private static final String MINUS = "-";

    private static String switchDateFormat(String dateString) {
        int length = dateString.length();
        switch (length) {
            case 19 -> {
                if (dateString.contains(MINUS)) {
                    return DateUtils.DATE_FORMAT_19;
                } else {
                    return DateUtils.DATE_FORMAT_19_FORWARD_SLASH;
                }
            }
            case 17 -> {
                return DateUtils.DATE_FORMAT_17;
            }
            case 14 -> {
                return DateUtils.DATE_FORMAT_14;
            }
            case 10 -> {
                return DateUtils.DATE_FORMAT_10;
            }
            default -> throw new IllegalArgumentException("Can not find date format for：" + dateString);
        }
    }

    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) throws ParseException {
        String stringValue = cellData.getStringValue();
        String pattern;
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            pattern = switchDateFormat(stringValue);
        } else {
            pattern = contentProperty.getDateTimeFormatProperty().getFormat();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(cellData.getStringValue(), formatter);
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        String pattern;
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            pattern = DateUtils.DATE_FORMAT_19;
        } else {
            pattern = contentProperty.getDateTimeFormatProperty().getFormat();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return new WriteCellData<>(value.format(formatter));
    }
}
    