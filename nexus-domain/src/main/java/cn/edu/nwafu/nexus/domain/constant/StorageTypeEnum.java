package cn.edu.nwafu.nexus.domain.constant;

import lombok.Getter;

/**
 * 存储类型枚举类
 *
 * @author Huang Z.Y.
 */
@Getter
public enum StorageTypeEnum {
    LOCAL(0, "本地存储"),
    ALIYUN_OSS(1, "阿里云 OSS 对象存储"),
    FAST_DFS(2, "fastDFS 集群存储"),
    MINIO(3, "minio 存储"),
    QINIUYUN_KODO(4, "七牛云 KODO 对象存储");
    private final int code;
    private final String name;

    StorageTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
