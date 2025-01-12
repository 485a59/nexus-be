package cn.edu.nwafu.nexus.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity 白名单资源路径配置。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {
    /**
     * 白名单路径。
     */
    private List<String> urls = new ArrayList<>();
}
