package cn.edu.nwafu.nexus.common.excel.aop;

import cn.edu.nwafu.nexus.common.excel.annotation.ResponseExcel;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理带有 {@link ResponseExcel @ResponseExcel} 注解的方法的返回值，并将数据写入 Excel 文件。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseExcelReturnValueHandler implements HandlerMethodReturnValueHandler {
    // 存储 Sheet 写入处理器的列表
    private final List<SheetWriteHandler> sheetWriteHandlerList;


    /**
     * 仅处理声明了 {@link ResponseExcel @ResponseExcel} 注解的方法。
     *
     * @param returnType 方法签名
     * @return 是否处理该方法的返回值
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 检查方法是否被 ResponseExcel 注解标注
        return returnType.getMethodAnnotation(ResponseExcel.class) != null;
    }


    /**
     * 处理逻辑。
     *
     * @param o                返回的参数
     * @param parameter        方法签名
     * @param mavContainer     上下文容器
     * @param nativeWebRequest Web 请求
     */
    @Override
    public void handleReturnValue(Object o, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest) throws Exception {
        // 获取 HttpServletResponse 对象，用于将 Excel 文件写入 HTTP 响应
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        // 断言确保 response 不为空
        Assert.state(response != null, "没有 HttpServletResponse");
        // 获取 ResponseExcel 注解
        ResponseExcel responseExcel = parameter.getMethodAnnotation(ResponseExcel.class);
        // 断言确保注解不为空
        Assert.state(responseExcel != null, "没有 @ResponseExcel 注解");
        // 标记请求已被处理，避免 Spring 后续的视图解析和渲染
        mavContainer.setRequestHandled(true);


        // 遍历 sheetWriteHandlerList，找到第一个支持当前返回值类型的处理器
        sheetWriteHandlerList.stream()
                .filter(handler -> handler.support(o))
                // 找到第一个满足条件的处理器
                .findFirst()
                // 如果找到合适的处理器，调用其 export 方法进行数据导出
                .ifPresent(handler -> handler.export(o, response, responseExcel));
    }
}