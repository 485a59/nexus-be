package cn.edu.nwafu.nexus.common.excel.handler;

import cn.edu.nwafu.nexus.common.excel.annotation.ResponseExcel;
import cn.edu.nwafu.nexus.common.excel.annotation.Sheet;
import cn.edu.nwafu.nexus.common.excel.aop.DynamicNameAspect;
import cn.edu.nwafu.nexus.common.excel.converter.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

/**
 * 导出 Excel，配置并创建 {@link ExcelWriter} 并将数据写入 Excel 文件。
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @SneakyThrows(UnsupportedEncodingException.class)
    @Override
    public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
        // 获取当前请求的上下文属性
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从请求属性中获取 Excel 文件名
        String name = (String) Objects.requireNonNull(requestAttributes)
                .getAttribute(DynamicNameAspect.EXCEL_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
        if (name == null) {
            // 生成一个随机的 UUID 作为文件名
            name = UUID.randomUUID().toString();
        }
        // 使用 UTF-8 编码格式对文件名进行编码，并添加文件后缀
        String fileName = String.format("%s%s", URLEncoder.encode(name, "UTF-8"), responseExcel.suffix().getValue());
        // 根据实际文件类型找到对应的 contentType
        String contentType = MediaTypeFactory.getMediaType(fileName)
                .map(MediaType::toString)
                .orElse("application/vnd.ms-excel");
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        // 将对象写入响应流
        write(o, response, responseExcel);
    }


    /**
     * 获取一个 {@link ExcelWriter} 的通用方法。
     *
     * @param response      HttpServletResponse
     * @param responseExcel ResponseExcel 注解
     * @return ExcelWriter
     */
    @SneakyThrows(IOException.class)
    public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
        // 创建 ExcelWriterBuilder 实例并注册通用转换器
        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream())
                .registerConverter(LocalDateStringConverter.INSTANCE)
                .registerConverter(LocalDateTimeStringConverter.INSTANCE)
                .registerConverter(LocalTimeStringConverter.INSTANCE)
                .registerConverter(LongStringConverter.INSTANCE)
                .registerConverter(StringArrayConverter.INSTANCE)
                .autoCloseStream(true)
                .excelType(responseExcel.suffix())
                .inMemory(responseExcel.inMemory());

        return writerBuilder.build();
    }

    /**
     * 获取 {@link WriteSheet} 对象。
     *
     * @param sheet     Sheet 注解信息
     * @param dataClass 数据类型
     * @return WriteSheet
     */
    public WriteSheet sheet(Sheet sheet, Class<?> dataClass) {
        // Sheet 的编号和名称
        Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
        String sheetName = sheet.sheetName();

        ExcelWriterSheetBuilder writerSheetBuilder = EasyExcel.writerSheet(sheetNo, sheetName);

        return writerSheetBuilder.build();
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}