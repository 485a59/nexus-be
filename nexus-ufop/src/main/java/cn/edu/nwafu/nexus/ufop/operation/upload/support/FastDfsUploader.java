package cn.edu.nwafu.nexus.ufop.operation.upload.support;

import cn.edu.nwafu.nexus.common.service.RedisService;
import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import cn.edu.nwafu.nexus.ufop.exception.operation.UploadException;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
import cn.edu.nwafu.nexus.ufop.operation.upload.request.UploadMultipartFile;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * FastDFS 文件上传类。
 *
 * @author Huang Z.Y.
 */
@Component
@Slf4j
public class FastDfsUploader extends Uploader {
    @Resource
    AppendFileStorageClient defaultAppendFileStorageClient;
    @Resource
    RedisService redisService;
    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public void doUploadFileChunk(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) throws IOException {
        StorePath storePath;
        if (uploadFile.getChunkNumber() <= 1) {
            log.info("上传第一块");
            storePath = defaultAppendFileStorageClient.uploadAppenderFile("group1", uploadMultipartFile.getUploadInputStream(),
                    uploadMultipartFile.getSize(), uploadMultipartFile.getExtension());
            // 记录第一个分片上传的大小
            String uploadedSizeKey = "nexus:upload:uploaded_size:" + uploadFile.getIdentifier();
            redisService.set(uploadedSizeKey, String.valueOf(uploadMultipartFile.getSize()), 1000 * 60 * 60);

            log.info("第一块上传完成");
            if (storePath == null) {
                String chunkNumberKey = "nexus:upload:chunk_number:" + uploadFile.getIdentifier();
                redisService.set(chunkNumberKey, String.valueOf(uploadFile.getChunkNumber()), 1000 * 60 * 60);

                log.info("获取远程文件路径出错");
                throw new UploadException("获取远程文件路径出错");
            }

            String storagePathKey = "nexus:upload:storage_path:" + uploadFile.getIdentifier();
            redisService.set(storagePathKey, storePath.getPath(), 1000 * 60 * 60);

            log.info("上传文件 result = {}", storePath.getPath());
        } else {
            log.info("正在上传第{}块：", uploadFile.getChunkNumber());

            String storagePathKey = "nexus:upload:storage_path:" + uploadFile.getIdentifier();
            String path = redisService.get(storagePathKey).toString();

            if (path == null) {
                log.error("无法获取已上传服务器文件地址");
                throw new UploadException("无法获取已上传服务器文件地址");
            }

            String uploadedSizeKey = "nexus:upload:uploaded_size:" + uploadFile.getIdentifier();
            String uploadedSizeStr = redisService.get(uploadedSizeKey).toString();
            long alreadySize = Long.parseLong(uploadedSizeStr);

            // 追加方式实际实用如果中途出错多次,可能会出现重复追加情况,这里改成修改模式,即时多次传来重复文件块,依然可以保证文件拼接正确
            defaultAppendFileStorageClient.modifyFile("group1", path, uploadMultipartFile.getUploadInputStream(),
                    uploadMultipartFile.getSize(), alreadySize);
            // 记录分片上传的大小
            redisService.set(uploadedSizeKey, String.valueOf(alreadySize + uploadMultipartFile.getSize()), 1000 * 60 * 60);
        }
    }

    @Override
    protected UploadFileResult organizationalResults(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();

        String storagePathKey = "nexus:upload:storage_path:" + uploadFile.getIdentifier();
        String path = redisService.get(storagePathKey).toString();
        uploadFileResult.setFileUrl(path);
        uploadFileResult.setFileName(uploadMultipartFile.getFileName());
        uploadFileResult.setExtendName(uploadMultipartFile.getExtension());
        uploadFileResult.setFileSize(uploadFile.getTotalSize());
        if (uploadFile.getTotalChunks() == 1) {
            uploadFileResult.setFileSize(uploadMultipartFile.getSize());
        }
        uploadFileResult.setStorageType(StorageTypeEnum.FAST_DFS);
        uploadFileResult.setIdentifier(uploadFile.getIdentifier());

        if (uploadFile.getChunkNumber() == uploadFile.getTotalChunks()) {
            log.info("分片上传完成");
            String chunkNumberKey = "nexus:upload:chunk_number:" + uploadFile.getIdentifier();
            redisService.del(chunkNumberKey);
            redisService.del(storagePathKey);
            String uploadedSizeKey = "nexus:upload:uploaded_size:" + uploadFile.getIdentifier();
            redisService.del(uploadedSizeKey);
            if (UFOPUtils.isImageFile(uploadFileResult.getExtendName())) {
                String group = "group1";
                String path1 = uploadFileResult.getFileUrl().substring(uploadFileResult.getFileUrl().indexOf("/") + 1);
                DownloadByteArray downloadByteArray = new DownloadByteArray();
                byte[] bytes = defaultAppendFileStorageClient.downloadFile(group, path1, downloadByteArray);
                InputStream is = new ByteArrayInputStream(bytes);

                BufferedImage src;
                try {
                    src = ImageIO.read(is);
                    uploadFileResult.setBufferedImage(src);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
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

    @Override
    public void cancelUpload(UploadFile uploadFile) {
        String storagePathKey = "nexus:upload:storage_path:" + uploadFile.getIdentifier();
        String path = redisService.get(storagePathKey).toString();
        try {
            fastFileStorageClient.deleteFile(path.replace("M00", "group1"));
        } catch (FdfsServerException e) {
            log.error(e.getMessage());
        }
    }
}
