package cn.edu.nwafu.nexus.ufop.config;

import cn.edu.nwafu.nexus.ufop.domain.AliyunOss;
import lombok.Data;

/**
 * 阿里云配置。
 *
 * @author Huang Z.Y.
 */
@Data
public class AliyunConfig {
    private AliyunOss oss = new AliyunOss();
}
