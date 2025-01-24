package cn.edu.nwafu.nexus.ufop.operation.download.domain;

import com.aliyun.oss.OSS;
import lombok.Data;

/**
 * 文件下载对象。
 *
 * @author Huang Z.Y.
 */
@Data
public class DownloadFile {
    private String fileUrl;
    private OSS ossClient;
    private Range range;
}
