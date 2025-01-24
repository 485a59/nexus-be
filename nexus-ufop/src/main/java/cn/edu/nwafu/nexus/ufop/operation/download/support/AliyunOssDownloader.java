package cn.edu.nwafu.nexus.ufop.operation.download.support;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;

import java.io.InputStream;

/**
 * 阿里云文件下载实现类。
 *
 * @author Huang Z.Y.
 */
public class AliyunOssDownloader extends Downloader {
    private AliyunConfig aliyunConfig;

    public AliyunOssDownloader() {
    }

    public AliyunOssDownloader(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {

        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject;
        if (downloadFile.getRange() != null) {
            GetObjectRequest getObjectRequest = new GetObjectRequest(aliyunConfig.getOss().getBucketName(),
                    UFOPUtils.getAliyunObjectNameByFileUrl(downloadFile.getFileUrl()));
            getObjectRequest.setRange(downloadFile.getRange().getStart(),
                    downloadFile.getRange().getStart() + downloadFile.getRange().getLength() - 1);
            ossObject = ossClient.getObject(getObjectRequest);
        } else {
            ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                    UFOPUtils.getAliyunObjectNameByFileUrl(downloadFile.getFileUrl()));
        }

        InputStream inputStream = ossObject.getObjectContent();

        downloadFile.setOssClient(ossClient);
        return inputStream;
    }
}
