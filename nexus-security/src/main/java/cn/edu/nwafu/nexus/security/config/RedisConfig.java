package cn.edu.nwafu.nexus.security.config;

import cn.edu.nwafu.nexus.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置。
 *
 * @author Huang Z.Y.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {
}
