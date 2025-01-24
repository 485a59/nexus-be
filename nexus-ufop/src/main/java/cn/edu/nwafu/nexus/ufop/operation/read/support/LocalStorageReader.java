package cn.edu.nwafu.nexus.ufop.operation.read.support;

import cn.edu.nwafu.nexus.ufop.exception.operation.ReadException;
import cn.edu.nwafu.nexus.ufop.operation.read.Reader;
import cn.edu.nwafu.nexus.ufop.operation.read.domain.ReadFile;
import cn.edu.nwafu.nexus.ufop.util.ReadFileUtils;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 本地读取文件实现类。
 *
 * @author Huang Z.Y.
 */
public class LocalStorageReader extends Reader {
    @Override
    public String read(ReadFile readFile) {

        String fileContent;
        FileInputStream fileInputStream = null;
        try {
            String extendName = FilenameUtils.getExtension(readFile.getFileUrl());
            fileInputStream = new FileInputStream(UFOPUtils.getStaticPath() + readFile.getFileUrl());
            fileContent = ReadFileUtils.getContentByInputStream(extendName, fileInputStream);
        } catch (IOException e) {
            throw new ReadException("文件读取出现异常", e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        return fileContent;
    }
}
