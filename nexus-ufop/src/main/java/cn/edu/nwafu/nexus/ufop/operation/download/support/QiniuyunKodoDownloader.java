package cn.edu.nwafu.nexus.ufop.operation.download.support;

import cn.edu.nwafu.nexus.common.util.HttpsUtils;
import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云文件下载实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class QiniuyunKodoDownloader extends Downloader {
    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoDownloader() {
    }

    public QiniuyunKodoDownloader(QiniuyunConfig qiniuyunConfig) {
        this.qiniuyunConfig = qiniuyunConfig;
    }

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {
        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());

        String urlString = auth.privateDownloadUrl(qiniuyunConfig.getKodo().getDomain() + "/" + downloadFile.getFileUrl());

        InputStream inputStream = HttpsUtils.doGet(urlString, null);
        try {
            if (downloadFile.getRange() != null) {
                inputStream.skip(downloadFile.getRange().getStart());
                byte[] bytes = new byte[downloadFile.getRange().getLength()];
                IOUtils.read(inputStream, bytes);
                inputStream = new ByteArrayInputStream(bytes);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return inputStream;
    }
}

