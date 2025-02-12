package cn.edu.nwafu.nexus.ufop.operation.write.support;

import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.exception.operation.WriteException;
import cn.edu.nwafu.nexus.ufop.operation.write.Writer;
import cn.edu.nwafu.nexus.ufop.operation.write.domain.WriteFile;
import cn.edu.nwafu.nexus.ufop.util.QiniuyunUtils;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云文件写入实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class QiniuyunKodoWriter extends Writer {
    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoWriter() {
    }

    public QiniuyunKodoWriter(QiniuyunConfig qiniuyunConfig) {
        this.qiniuyunConfig = qiniuyunConfig;
    }

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        qiniuUpload(writeFile.getFileUrl(), inputStream);
    }

    private void qiniuUpload(String fileUrl, InputStream inputStream) {
        Configuration cfg = QiniuyunUtils.getCfg(qiniuyunConfig);
        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());
        String upToken = auth.uploadToken(qiniuyunConfig.getKodo().getBucketName(), fileUrl);

        String localTempDir = UFOPUtils.getStaticPath() + "temp";
        try {
            // 设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            Response response = uploadManager.put(inputStream, fileUrl, upToken, null, null);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info(putRet.key);
            log.info(putRet.hash);
        } catch (IOException ex) {
            throw new WriteException("七牛云写文件失败", ex);
        }
    }
}
