package cn.edu.nwafu.nexus.ufop.operation.preview.support;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 阿里云文件预览实现类。
 *
 * @author Huang Z.Y.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class AliyunOssPreviewer extends Previewer {
    private AliyunConfig aliyunConfig;

    public AliyunOssPreviewer() {
    }

    public AliyunOssPreviewer(AliyunConfig aliyunConfig, ThumbImage thumbImage) {
        this.aliyunConfig = aliyunConfig;
        setThumbImage(thumbImage);
    }

    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                UFOPUtils.getAliyunObjectNameByFileUrl(previewFile.getFileUrl()));
        InputStream inputStream = ossObject.getObjectContent();
        previewFile.setOssClient(ossClient);
        return inputStream;
    }
}

