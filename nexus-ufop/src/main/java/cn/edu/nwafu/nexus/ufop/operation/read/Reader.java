package cn.edu.nwafu.nexus.ufop.operation.read;

import cn.edu.nwafu.nexus.ufop.operation.read.domain.ReadFile;

/**
 * 文件读取抽象类。
 *
 * @author Huang Z.Y.
 */
public abstract class Reader {
    public abstract String read(ReadFile readFile);
}
