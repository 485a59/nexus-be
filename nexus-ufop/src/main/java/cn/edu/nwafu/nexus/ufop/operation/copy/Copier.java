package cn.edu.nwafu.nexus.ufop.operation.copy;

import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;

import java.io.InputStream;

/**
 * 文件上传抽象类。
 *
 * @author Huang Z.Y.
 */
public abstract class Copier {
    /**
     * 将服务器文件流拷贝到云端，并返回文件 url。
     *
     * @param inputStream 文件流
     * @param copyFile    拷贝文件相关参数
     * @return 文件 url
     */
    public abstract String copy(InputStream inputStream, CopyFile copyFile);
}
