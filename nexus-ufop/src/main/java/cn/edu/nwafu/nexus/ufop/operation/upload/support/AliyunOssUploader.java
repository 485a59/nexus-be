package cn.edu.nwafu.nexus.ufop.operation.upload.support;

import cn.edu.nwafu.nexus.common.service.RedisService;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileInfo;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
import cn.edu.nwafu.nexus.ufop.operation.upload.request.UploadMultipartFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 阿里云文件上传实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class AliyunOssUploader extends Uploader {
    @Resource
    private RedisService redisService;

    private AliyunConfig aliyunConfig;

    public AliyunOssUploader() {
    }

    public AliyunOssUploader(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    protected void doUploadFileChunk(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) throws IOException {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            String uploadPartRequestKey = "nexus:upload:part_request:" + uploadFile.getIdentifier();
            Object uploadPartRequestKeyValue = redisService.get(uploadPartRequestKey);
            UploadFileInfo uploadFileInfo = null;
            if (uploadPartRequestKeyValue != null) {
                uploadFileInfo = JSON.parseObject(uploadPartRequestKeyValue.toString(), UploadFileInfo.class);
            }
            String fileUrl = uploadMultipartFile.getFileUrl();
            if (uploadFileInfo == null) {
                InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(aliyunConfig.getOss().getBucketName(), fileUrl);
                InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
                String uploadId = upresult.getUploadId();

                uploadFileInfo = new UploadFileInfo();
                uploadFileInfo.setBucketName(aliyunConfig.getOss().getBucketName());
                uploadFileInfo.setKey(fileUrl);
                uploadFileInfo.setUploadId(uploadId);

                redisService.set(uploadPartRequestKey, JSON.toJSONString(uploadFileInfo));
            }

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(uploadFileInfo.getBucketName());
            uploadPartRequest.setKey(uploadFileInfo.getKey());
            uploadPartRequest.setUploadId(uploadFileInfo.getUploadId());
            uploadPartRequest.setInputStream(uploadMultipartFile.getUploadInputStream());
            uploadPartRequest.setPartSize(uploadMultipartFile.getSize());
            uploadPartRequest.setPartNumber(uploadFile.getChunkNumber() + 1);
            log.debug(JSON.toJSONString(uploadPartRequest));

            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);

            log.info("上传结果：{}", JSON.toJSONString(uploadPartResult));

            String partETagsKey = "nexus:upload:part_etags:" + uploadFile.getIdentifier();
            if (redisService.hasKey(partETagsKey)) {
                List<PartETag> partETags = JSON.parseArray(redisService.get(partETagsKey).toString(), PartETag.class);
                partETags.add(uploadPartResult.getPartETag());
                redisService.set(partETagsKey, JSON.toJSONString(partETags));
            } else {
                List<PartETag> partETags = new ArrayList<>();
                partETags.add(uploadPartResult.getPartETag());
                redisService.set(partETagsKey, JSON.toJSONString(partETags));
            }
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    protected UploadFileResult organizationalResults(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        String uploadPartRequestKey = "nexus:upload:part_request:" + uploadFile.getIdentifier();
        UploadFileInfo uploadFileInfo = JSON.parseObject(redisService.get(uploadPartRequestKey).toString(), UploadFileInfo.class);

        uploadFileResult.setFileUrl(uploadFileInfo.getKey());
        uploadFileResult.setFileName(uploadMultipartFile.getFileName());
        uploadFileResult.setExtension(uploadMultipartFile.getExtension());
        uploadFileResult.setFileSize(uploadFile.getTotalSize());
        if (uploadFile.getTotalChunks() == 1) {
            uploadFileResult.setFileSize(uploadMultipartFile.getSize());
        }
        uploadFileResult.setStorageType(StorageTypeEnum.ALIYUN_OSS);
        uploadFileResult.setIdentifier(uploadFile.getIdentifier());
        if (uploadFile.getChunkNumber() == uploadFile.getTotalChunks() - 1) {
            log.info("分片上传完成");
            completeMultipartUpload(uploadFile);
            redisService.del("nexus:upload:chunk_number:" + uploadFile.getIdentifier());
            redisService.del("nexus:upload:part_etags:" + uploadFile.getIdentifier());
            redisService.del(uploadPartRequestKey);
            if (UFOPUtils.isImageFile(uploadFileResult.getExtension())) {
                OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
                OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                        UFOPUtils.getAliyunObjectNameByFileUrl(uploadFileResult.getFileUrl()));
                InputStream is = ossObject.getObjectContent();
                BufferedImage src;
                try {
                    src = ImageIO.read(is);
                    uploadFileResult.setBufferedImage(src);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
            uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
        } else {
            uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLETED);
        }
        return uploadFileResult;
    }

    /**
     * 将文件分块进行升序排序并执行文件上传。
     *
     * @param uploadFile 上传信息
     */
    private void completeMultipartUpload(UploadFile uploadFile) {
        String partETagsKey = "nexus:upload:part_etags:" + uploadFile.getIdentifier();
        List<PartETag> partETags = JSON.parseArray(redisService.get(partETagsKey).toString(), PartETag.class);

        partETags.sort(Comparator.comparingInt(PartETag::getPartNumber));

        String uploadPartRequestKey = "nexus:upload:part_request:" + uploadFile.getIdentifier();
        UploadFileInfo uploadFileInfo = JSON.parseObject(redisService.get(uploadPartRequestKey).toString(), UploadFileInfo.class);

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(aliyunConfig.getOss().getBucketName(),
                        uploadFileInfo.getKey(),
                        uploadFileInfo.getUploadId(),
                        partETags);
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        // 完成上传
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        ossClient.shutdown();
    }

    /**
     * 取消上传。
     */
    @Override
    public void cancelUpload(UploadFile uploadFile) {
        String uploadPartRequestKey = "nexus:upload:part_request:" + uploadFile.getIdentifier();
        UploadFileInfo uploadFileInfo = JSON.parseObject(redisService.get(uploadPartRequestKey).toString(), UploadFileInfo.class);

        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        AbortMultipartUploadRequest abortMultipartUploadRequest =
                new AbortMultipartUploadRequest(aliyunConfig.getOss().getBucketName(),
                        uploadFileInfo.getKey(),
                        uploadFileInfo.getUploadId());
        ossClient.abortMultipartUpload(abortMultipartUploadRequest);
        ossClient.shutdown();
    }
}