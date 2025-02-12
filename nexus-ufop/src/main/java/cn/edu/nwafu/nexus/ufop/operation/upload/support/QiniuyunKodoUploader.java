package cn.edu.nwafu.nexus.ufop.operation.upload.support;

import cn.edu.nwafu.nexus.common.service.RedisService;
import cn.edu.nwafu.nexus.common.util.HttpsUtils;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import cn.edu.nwafu.nexus.ufop.exception.UFOPException;
import cn.edu.nwafu.nexus.ufop.exception.operation.UploadException;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
import cn.edu.nwafu.nexus.ufop.operation.upload.request.UploadMultipartFile;
import cn.edu.nwafu.nexus.ufop.util.QiniuyunUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云文件上传实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class QiniuyunKodoUploader extends Uploader {
    @Resource
    RedisService redisService;
    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoUploader() {
    }

    public QiniuyunKodoUploader(QiniuyunConfig qiniuyunConfig) {
        this.qiniuyunConfig = qiniuyunConfig;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
    }

    @Override
    protected void doUploadFileChunk(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {

    }

    @Override
    protected UploadFileResult organizationalResults(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        return null;
    }

    protected UploadFileResult doUploadFlow(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            uploadMultipartFile.getFileUrl(uploadFile.getIdentifier());
            String fileUrl = UFOPUtils.getUploadFileUrl(uploadFile.getIdentifier(), uploadMultipartFile.getExtension());

            File tempFile = UFOPUtils.getTempFile(fileUrl);
            File processFile = UFOPUtils.getProcessFile(fileUrl);

            byte[] fileData = uploadMultipartFile.getUploadBytes();

            writeByteDataToFile(fileData, tempFile, uploadFile);

            //判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, processFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(uploadMultipartFile.getFileName());
            uploadFileResult.setExtension(uploadMultipartFile.getExtension());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnum.QINIUYUN_KODO);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(uploadMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {

                qiniuUpload(fileUrl, tempFile, uploadFile);
                uploadFileResult.setFileUrl(fileUrl);
                boolean result = tempFile.delete();
                if (!result) {
                    throw new UFOPException("删除temp文件失败：目录路径：" + tempFile.getPath());
                }

                if (UFOPUtils.isImageFile(uploadFileResult.getExtension())) {
                    Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());

                    String urlString = auth.privateDownloadUrl(qiniuyunConfig.getKodo().getDomain() + "/" + uploadFileResult.getFileUrl());

                    InputStream inputStream = HttpsUtils.doGet(urlString, null);
                    BufferedImage src;
                    try {
                        src = ImageIO.read(inputStream);
                        uploadFileResult.setBufferedImage(src);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }

                }

                uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
            } else {
                uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLETED);
            }
        } catch (IOException e) {
            throw new UploadException(e);
        }


        return uploadFileResult;
    }


    private void qiniuUpload(String fileUrl, File file, UploadFile uploadFile) {
        Configuration cfg = QiniuyunUtils.getCfg(qiniuyunConfig);
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.resumableUploadMaxConcurrentTaskCount = 2;  // 设置分片上传并发，1：采用同步上传；大于1：采用并发上传

        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());
        String upToken = auth.uploadToken(qiniuyunConfig.getKodo().getBucketName());

        String localTempDir = UFOPUtils.getStaticPath() + "temp";
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(file.getAbsoluteFile(), fileUrl, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                log.info(putRet.key);
                log.info(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}