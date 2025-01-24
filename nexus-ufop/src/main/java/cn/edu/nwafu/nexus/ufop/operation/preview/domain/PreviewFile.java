package cn.edu.nwafu.nexus.ufop.operation.preview.domain;

import com.aliyun.oss.OSS;
import lombok.Data;

/**
 * 预览文件对象。
 *
 * @author Huang Z.Y.
 */
@Data
public class PreviewFile {
    private String fileUrl;
    private OSS ossClient;
}
