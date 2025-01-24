package cn.edu.nwafu.nexus.ufop.operation.write.domain;

import lombok.Data;

/**
 * 文件写入对象。
 *
 * @author Huang Z.Y.
 */
@Data
public class WriteFile {
    private String fileUrl;
    private long fileSize;
}
