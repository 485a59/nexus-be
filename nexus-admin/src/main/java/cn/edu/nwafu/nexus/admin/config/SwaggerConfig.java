package cn.edu.nwafu.nexus.admin.config;

import cn.edu.nwafu.nexus.common.config.BaseSwaggerConfig;
import cn.edu.nwafu.nexus.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 相关配置。
 *
 * @author Huang Z.Y.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("cn.edu.nwafu.nexus.admin.controller")
                .title("nexus 后台管理系统")
                .description("nexus 后台管理相关接口文档")
                .contactName("nexus")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
