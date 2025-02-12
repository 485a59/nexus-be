package cn.edu.nwafu.nexus.common.util;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.annotation.ExcelSheet;
import cn.edu.nwafu.nexus.common.exception.ApiException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 导入导出工具类。
 *
 * @author Huang Z.Y.
 */
@UtilityClass
@Slf4j
public class ExcelUtils {

    /**
     * 导出到响应流。
     */
    public static <T> void writeToResponse(List<T> list, Class<T> clazz, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = getSheetName(clazz);
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" +
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + ".xlsx");

            writeToOutputStream(list, clazz, response.getOutputStream());
        } catch (IOException e) {
            throw new ApiException("导出Excel失败: " + e.getMessage());
        }
    }

    /**
     * 从请求中读取 Excel。
     */
    public static <T> List<T> readFromRequest(Class<T> clazz, MultipartFile file) {
        try {
            return readFromInputStream(clazz, file.getInputStream());
        } catch (IOException e) {
            throw new ApiException("读取Excel失败: " + e.getMessage());
        }
    }

    /**
     * 写入到输出流。
     */
    public static <T> void writeToOutputStream(List<T> list, Class<T> clazz, OutputStream outputStream) {
        try {
            // 获取自定义表头
            Map<String, String> headerAliasMap = getHeaderAlias(clazz);

            // 创建写入对象
            EasyExcel.write(outputStream, clazz)
                    .autoCloseStream(false)
                    // 自动适应列宽
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 设置自定义表头
                    .head(generateExcelHead(headerAliasMap))
                    // 设置 sheet 名称
                    .sheet(getSheetName(clazz))
                    .doWrite(list);

        } catch (Exception e) {
            throw new ApiException("写入Excel失败: " + e.getMessage());
        }
    }

    /**
     * 从输入流读取。
     */
    public static <T> List<T> readFromInputStream(Class<T> clazz, InputStream inputStream) {
        try {
            // 创建自定义表头映射
            Map<String, String> headerAliasMap = getHeaderAlias(clazz);
            // 读取数据
            return EasyExcel.read(inputStream)
                    .head(clazz)
                    // 设置自定义表头映射
                    .headRowNumber(1)
                    .sheet()
                    .doReadSync();

        } catch (Exception e) {
            throw new ApiException("读取Excel失败: " + e.getMessage());
        }
    }

    /**
     * 获取表头别名映射。
     */
    private static Map<String, String> getHeaderAlias(Class<?> clazz) {
        Map<String, String> headerAliasMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                headerAliasMap.put(field.getName(), annotation.name());
            }
        }
        return headerAliasMap;
    }

    /**
     * 生成 Excel 表头。
     */
    private static List<List<String>> generateExcelHead(Map<String, String> headerAliasMap) {
        List<List<String>> head = new ArrayList<>();
        headerAliasMap.values().forEach(headerName -> {
            List<String> column = new ArrayList<>();
            column.add(headerName);
            head.add(column);
        });
        return head;
    }

    /**
     * 获取 Sheet 名称。
     */
    private static <T> String getSheetName(Class<T> clazz) {
        ExcelSheet sheetAnno = clazz.getAnnotation(ExcelSheet.class);
        return sheetAnno != null ? sheetAnno.name() : "Sheet1";
    }
}
