package cn.edu.nwafu.nexus.ufop.operation.preview.support;

import cn.edu.nwafu.nexus.common.util.HttpsUtils;
import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import com.qiniu.util.Auth;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 七牛云文件预览实现类。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@Slf4j
public class QiniuyunKodoPreviewer extends Previewer {
    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoPreviewer() {
    }

    public QiniuyunKodoPreviewer(QiniuyunConfig qiniuyunConfig, ThumbImage thumbImage) {
        this.qiniuyunConfig = qiniuyunConfig;
        setThumbImage(thumbImage);
    }


    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());
        String urlString = auth.privateDownloadUrl(qiniuyunConfig.getKodo().getDomain() + "/" + previewFile.getFileUrl());
        return HttpsUtils.doGet(urlString, null);
    }
}
