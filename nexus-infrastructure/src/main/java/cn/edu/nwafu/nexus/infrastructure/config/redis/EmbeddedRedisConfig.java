package cn.edu.nwafu.nexus.infrastructure.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 嵌入式 Redis 配置类。
 * <p>
 * 该类用于在满足条件时配置和管理嵌入式 Redis 服务器。
 * 只有当配置属性 'nexus.embedded.redis' 的值为 'true' 时，此配置才会生效。
 */
@Configuration
@ConditionalOnExpression("'${nexus.embedded.redis}' == 'true'")
public class EmbeddedRedisConfig {
    /**
     * 从配置文件中注入 spring.redis.port 属性的值，该值表示 Redis 服务器的端口号
     */
    @Value("${spring.redis.port}")
    private Integer port;

    /**
     * 嵌入式 Redis 服务器实例
     */
    private RedisServer redisServer;

    /**
     * 初始化方法，在对象创建后调用
     * 用于启动嵌入式 Redis 服务器
     */
    @PostConstruct
    public void postConstruct() {
        // 创建 RedisServer 实例，设置端口和一些配置参数
        RedisServer redisServer = RedisServer.builder()
                .port(port)
                .setting("maxheap 32M")
                .setting("daemonize no")
                .setting("appendonly no")
                .build();
        this.redisServer = redisServer;
        // 启动 Redis 服务器
        redisServer.start();
    }

    /**
     * 销毁方法，在对象销毁前调用，用于停止嵌入式 Redis 服务器
     */
    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}