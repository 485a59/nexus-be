package cn.edu.nwafu.nexus.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Nexus 管理端启动类。
 *
 * @author Huang Z.Y.
 */
@SpringBootApplication(scanBasePackages = {
        "cn.edu.nwafu.nexus",
        "cn.edu.nwafu.nexus.security",
        "cn.edu.nwafu.nexus.ufop"})
@ComponentScan(basePackages = {"cn.edu.nwafu.nexus",
        "cn.edu.nwafu.nexus.infrastructure",
        "cn.edu.nwafu.nexus.security",
        "cn.edu.nwafu.nexus.ufop"})
public class NexusAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(NexusAdminApplication.class);
    }
}
