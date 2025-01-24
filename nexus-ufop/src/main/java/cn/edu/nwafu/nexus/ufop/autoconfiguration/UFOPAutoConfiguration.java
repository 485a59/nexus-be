package cn.edu.nwafu.nexus.ufop.autoconfiguration;

import cn.edu.nwafu.nexus.common.service.RedisService;
import cn.edu.nwafu.nexus.common.service.impl.RedisServiceImpl;
import com.github.tobato.fastdfs.FdfsClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * 统一文件操作平台自动装配类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Configuration
//@ConditionalOnClass(UFOService.class)
@EnableConfigurationProperties({UFOPProperties.class})
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class UFOPAutoConfiguration {
    @Bean
    public RedisService redisService() {
        return new RedisServiceImpl();
    }
}
