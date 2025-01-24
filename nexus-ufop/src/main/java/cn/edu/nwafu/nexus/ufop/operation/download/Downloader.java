package cn.edu.nwafu.nexus.ufop.operation.download;

import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import com.aliyun.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件下载抽象类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public abstract class Downloader {
    public void download(HttpServletResponse httpServletResponse, DownloadFile downloadFile) {
        InputStream inputStream = getInputStream(downloadFile);
        OutputStream outputStream = null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            IOUtils.copyLarge(inputStream, outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            OSS ossClient = downloadFile.getOssClient();
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public abstract InputStream getInputStream(DownloadFile downloadFile);
}