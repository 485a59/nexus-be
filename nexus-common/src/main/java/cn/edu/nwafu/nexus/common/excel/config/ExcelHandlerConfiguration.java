package cn.edu.nwafu.nexus.common.excel.config;

import cn.edu.nwafu.nexus.common.excel.aop.ResponseExcelReturnValueHandler;
import cn.edu.nwafu.nexus.common.excel.handler.SheetWriteHandler;
import cn.edu.nwafu.nexus.common.excel.handler.SingleSheetWriteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

/**
 * Excel 处理器配置。
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class ExcelHandlerConfiguration {
    /**
     * 单 sheet 写入处理器。
     */
    @Bean
    @ConditionalOnMissingBean
    public SingleSheetWriteHandler singleSheetWriteHandler() {
        return new SingleSheetWriteHandler();
    }

    /**
     * 返回 Excel 文件的响应处理程序。
     *
     * @param sheetWriteHandlerList 页签写入处理器集合
     * @return ResponseExcelReturnValueHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
            List<SheetWriteHandler> sheetWriteHandlerList) {
        return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }
}
    