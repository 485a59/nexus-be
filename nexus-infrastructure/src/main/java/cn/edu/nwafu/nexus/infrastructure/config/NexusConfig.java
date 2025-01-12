package cn.edu.nwafu.nexus.infrastructure.config;

import cn.edu.nwafu.nexus.common.constant.Constants;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Huang Z.Y.
 */
@Component
@ConfigurationProperties(prefix = "nexus")
@UtilityClass
public class NexusConfig {
    /**
     * 上传路径
     */
    @Getter
    private static String fileBaseDir;
    /**
     * 获取地址开关
     */
    @Getter
    private static boolean addressEnabled;
    /**
     * 验证码类型
     */
    private static String captchaType;
    /**
     * rsa private key  静态属性的注入！！ set方法一定不能是static 方法
     */
    private static String rsaPrivateKey;
    private static String apiPrefix;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 版本
     */
    private String version;

    public void setRsaPrivateKey(String rsaPrivateKey) {
        NexusConfig.rsaPrivateKey = rsaPrivateKey;
    }

    public void setApiPrefix(String apiDocsPathPrefix) {
        NexusConfig.apiPrefix = apiDocsPathPrefix;
    }

    public void setCaptchaType(String captchaType) {
        NexusConfig.captchaType = captchaType;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        NexusConfig.addressEnabled = addressEnabled;
    }

    public void setFileBaseDir(String fileBaseDir) {
        NexusConfig.fileBaseDir = fileBaseDir + File.separator + Constants.RESOURCE_PREFIX;
    }
}
