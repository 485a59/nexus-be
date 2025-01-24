package cn.edu.nwafu.nexus.ufop.config;

import cn.edu.nwafu.nexus.ufop.domain.QiniuyunKodo;
import lombok.Data;

/**
 * 七牛云配置。
 *
 * @author Huang Z.Y.
 */
@Data
public class QiniuyunConfig {
    private QiniuyunKodo kodo = new QiniuyunKodo();
}
