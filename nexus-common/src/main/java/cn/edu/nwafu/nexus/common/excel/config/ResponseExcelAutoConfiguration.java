package cn.edu.nwafu.nexus.common.excel.config;

import cn.edu.nwafu.nexus.common.excel.aop.DynamicNameAspect;
import cn.edu.nwafu.nexus.common.excel.aop.RequestExcelArgumentResolver;
import cn.edu.nwafu.nexus.common.excel.aop.ResponseExcelReturnValueHandler;
import cn.edu.nwafu.nexus.common.excel.processor.NameProcessor;
import cn.edu.nwafu.nexus.common.excel.processor.NameSpelExpressionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置初始化。
 *
 * @author Huang Z.Y.
 */
@AutoConfiguration
@RequiredArgsConstructor
@Import(ExcelHandlerConfiguration.class)
public class ResponseExcelAutoConfiguration {
    /**
     * 注入 RequestMappingHandlerAdapter  Bean 以配置返回值处理器
     */
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 注入自定义的 ResponseExcelReturnValueHandler 来处理 Excel 响应
     */
    private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

    /**
     * NameProcessor 的 Bean 定义，用于处理 SpEL。
     *
     * @return NameProcessor Excel 名称解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public NameProcessor nameProcessor() {
        return new NameSpelExpressionProcessor();
    }

    /**
     * DynamicNameAspect 的 Bean 定义，它使用 NameProcessor 来处理 Excel 文件的动态名称生成。
     *
     * @param nameProcessor SPEL 解析处理器
     * @return DynamicNameAspect
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
        return new DynamicNameAspect(nameProcessor);
    }

    /**
     * 将 Excel 的返回值处理器添加到 Spring MVC。
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
                .getReturnValueHandlers();


        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(responseExcelReturnValueHandler);
        assert returnValueHandlers != null;
        newHandlers.addAll(returnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

    /**
     * 将 Excel 的请求处理器添加到 Spring MVC。
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
        resolverList.add(new RequestExcelArgumentResolver());
        resolverList.addAll(argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
    }
}