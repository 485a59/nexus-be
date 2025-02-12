package cn.edu.nwafu.nexus.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Huang Z.Y.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 允许本地端口的 cors
                .allowedMethods("*") // 允许所有方法（或明确指定 POST, OPTIONS）
                .allowedHeaders("*") // 允许所有头（包括 Authorization）
                .allowCredentials(true)
                .maxAge(3600);
    }
}
