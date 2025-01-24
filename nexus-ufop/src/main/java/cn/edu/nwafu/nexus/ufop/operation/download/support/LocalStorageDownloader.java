package cn.edu.nwafu.nexus.ufop.operation.download.support;

import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * 本地文件下载实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class LocalStorageDownloader extends Downloader {
    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {
        //设置文件路径
        File file = new File(UFOPUtils.getStaticPath() + downloadFile.getFileUrl());

        InputStream inputStream = null;
        byte[] bytes = new byte[0];
        RandomAccessFile randowAccessFile = null;
        try {
            if (downloadFile.getRange() != null) {
                randowAccessFile = new RandomAccessFile(file, "r");
                randowAccessFile.seek(downloadFile.getRange().getStart());
                bytes = new byte[downloadFile.getRange().getLength()];
                randowAccessFile.read(bytes);
            } else {
                inputStream = new FileInputStream(file);
                bytes = IOUtils.toByteArray(inputStream);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(randowAccessFile);
        }
        return new ByteArrayInputStream(bytes);
    }
}

