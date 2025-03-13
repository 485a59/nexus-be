package cn.edu.nwafu.nexus.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 前台系统启动类。
 *
 * @author Huang Z.Y.
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.nwafu.nexus"})
@ComponentScan(basePackages = {"cn.edu.nwafu.nexus", "cn.edu.nwafu.nexus.infrastructure"})
@EnableScheduling  // 启用定时任务
public class NexusChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(NexusChatApplication.class, args);
    }
}
