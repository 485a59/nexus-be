package cn.edu.nwafu.nexus.ufop.operation.write;

import cn.edu.nwafu.nexus.ufop.operation.write.domain.WriteFile;

import java.io.InputStream;

/**
 * 文件写入抽象类。
 *
 * @author Huang Z.Y.
 */
public abstract class Writer {
    public abstract void write(InputStream inputStream, WriteFile writeFile);
}
