package cn.edu.nwafu.nexus.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public Executor fileTransferExecutor() {
        return Executors.newFixedThreadPool(20);
    }
} 