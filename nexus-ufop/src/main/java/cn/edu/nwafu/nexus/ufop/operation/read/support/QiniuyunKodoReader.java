package cn.edu.nwafu.nexus.ufop.operation.read.support;

import cn.edu.nwafu.nexus.common.util.HttpsUtils;
import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.exception.operation.ReadException;
import cn.edu.nwafu.nexus.ufop.operation.read.Reader;
import cn.edu.nwafu.nexus.ufop.operation.read.domain.ReadFile;
import cn.edu.nwafu.nexus.ufop.util.ReadFileUtils;
import com.qiniu.util.Auth;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云文件读取实现类。
 *
 * @author Huang Z.Y.
 */
public class QiniuyunKodoReader extends Reader {
    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoReader() {
    }

    public QiniuyunKodoReader(QiniuyunConfig qiniuyunConfig) {
        this.qiniuyunConfig = qiniuyunConfig;
    }

    @Override
    public String read(ReadFile readFile) {
        String fileUrl = readFile.getFileUrl();
        String fileType = FilenameUtils.getExtension(fileUrl);
        try {
            return ReadFileUtils.getContentByInputStream(fileType, getInputStream(readFile.getFileUrl()));
        } catch (IOException e) {
            throw new ReadException("读取文件失败", e);
        }
    }

    public InputStream getInputStream(String fileUrl) {
        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());
        String urlString = auth.privateDownloadUrl(qiniuyunConfig.getKodo().getDomain() + "/" + fileUrl);
        return HttpsUtils.doGet(urlString, null);
    }
}

