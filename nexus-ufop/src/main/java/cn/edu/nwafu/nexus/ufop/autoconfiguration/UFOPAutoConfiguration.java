package cn.edu.nwafu.nexus.ufop.autoconfiguration;

import cn.edu.nwafu.nexus.common.service.RedisService;
import cn.edu.nwafu.nexus.common.service.impl.RedisServiceImpl;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import cn.edu.nwafu.nexus.ufop.operation.copy.support.FastDfsCopier;
import cn.edu.nwafu.nexus.ufop.operation.delete.support.FastDfsDeleter;
import cn.edu.nwafu.nexus.ufop.operation.download.support.FastDfsDownloader;
import cn.edu.nwafu.nexus.ufop.operation.preview.support.FastDfsPreviewer;
import cn.edu.nwafu.nexus.ufop.operation.read.support.FastDfsReader;
import cn.edu.nwafu.nexus.ufop.operation.upload.support.AliyunOssUploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.support.FastDfsUploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.support.MinioUploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.support.QiniuyunKodoUploader;
import cn.edu.nwafu.nexus.ufop.operation.write.support.FastDfsWriter;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.FdfsClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import javax.annotation.Resource;

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
    @Resource
    private UFOPProperties ufopProperties;

    @Bean
    public RedisService redisService() {
        return new RedisServiceImpl();
    }

    @Bean
    public FastDfsCopier fastDfsCreater() {
        return new FastDfsCopier();
    }

    @Bean
    public FastDfsUploader fastDFSUploader() {
        return new FastDfsUploader();
    }

    @Bean
    public FastDfsDownloader fastDFSDownloader() {
        return new FastDfsDownloader();
    }

    @Bean
    public FastDfsDeleter fastDFSDeleter() {
        return new FastDfsDeleter();
    }

    @Bean
    public FastDfsReader fastDFSReader() {
        return new FastDfsReader();
    }

    @Bean
    public FastDfsWriter fastDFSWriter() {
        return new FastDfsWriter();
    }

    @Bean
    public FastDfsPreviewer fastDFSPreviewer() {
        return new FastDfsPreviewer(ufopProperties.getThumbImage());
    }

    @Bean
    public AliyunOssUploader aliyunOSSUploader() {
        return new AliyunOssUploader(ufopProperties.getAliyun());
    }

    @Bean
    public MinioUploader minioUploader() {
        return new MinioUploader(ufopProperties.getMinio());
    }

    @Bean
    public QiniuyunKodoUploader qiniuyunKodoUploader() {
        return new QiniuyunKodoUploader(ufopProperties.getQiniuyun());
    }

    @Bean
    public UFOPFactory ufopFactory() {
        UFOPUtils.LOCAL_STORAGE_PATH = ufopProperties.getLocalStoragePath();
        String bucketName = ufopProperties.getBucketName();
        if (StrUtil.isNotEmpty(bucketName)) {
            UFOPUtils.ROOT_PATH = ufopProperties.getBucketName();
        } else {
            UFOPUtils.ROOT_PATH = "upload";
        }
        return new UFOPFactory(ufopProperties);
    }
}
