package cn.edu.nwafu.nexus.ufop.operation.upload.support;

import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.constant.UploadFileStatusEnum;
import cn.edu.nwafu.nexus.ufop.exception.operation.UploadException;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFile;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
import cn.edu.nwafu.nexus.ufop.operation.upload.request.UploadMultipartFile;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地文件上传实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class LocalStorageUploader extends Uploader {
    /**
     * 用于存储文件标识符与文件URL的映射关系。
     */
    public static Map<String, String> FILE_URL_MAP = new HashMap<>();

    @Override
    protected UploadFileResult doUploadFlow(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            String fileUrl = UFOPUtils.getUploadFileUrl(uploadFile.getIdentifier(), uploadMultipartFile.getExtension());
            if (StringUtils.isNotEmpty(FILE_URL_MAP.get(uploadFile.getIdentifier()))) {
                fileUrl = FILE_URL_MAP.get(uploadFile.getIdentifier());
            } else {
                FILE_URL_MAP.put(uploadFile.getIdentifier(), fileUrl);
            }
            String tempFileUrl = fileUrl + "_tmp";
            String confFileUrl = fileUrl.replace("." + uploadMultipartFile.getExtension(), ".conf");

            File file = new File(UFOPUtils.getStaticPath() + fileUrl);
            File tempFile = new File(UFOPUtils.getStaticPath() + tempFileUrl);
            File confFile = new File(UFOPUtils.getStaticPath() + confFileUrl);

            // 1. 打开将要写入的文件
            RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
            // 2. 打开通道
            try {
                FileChannel fileChannel = raf.getChannel();
                // 3. 计算偏移量
                long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
                // 4. 获取分片数据
                byte[] fileData = uploadMultipartFile.getUploadBytes();
                // 5. 写入数据
                fileChannel.position(position);
                fileChannel.write(ByteBuffer.wrap(fileData));
                fileChannel.force(true);
                fileChannel.close();
            } finally {
                IOUtils.closeQuietly(raf);
            }

            // 判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, confFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(uploadMultipartFile.getFileName());
            uploadFileResult.setExtendName(uploadMultipartFile.getExtension());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnum.LOCAL);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(uploadMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {
                tempFile.renameTo(file);
                FILE_URL_MAP.remove(uploadFile.getIdentifier());

                if (UFOPUtils.isImageFile(uploadFileResult.getExtendName())) {
                    InputStream is = null;
                    try {
                        is = new FileInputStream(UFOPUtils.getLocalSaveFile(fileUrl));
                        BufferedImage src = ImageIO.read(is);
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
        } catch (IOException e) {
            throw new UploadException(e);
        }

        return uploadFileResult;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
        String fileUrl = FILE_URL_MAP.get(uploadFile.getIdentifier());
        if (fileUrl != null) {
            String tempFileUrl = fileUrl + "_tmp";
            String confFileUrl = fileUrl.replace("." + FilenameUtils.getExtension(fileUrl), ".conf");
            File tempFile = new File(UFOPUtils.getStaticPath() + tempFileUrl);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            File confFile = new File(UFOPUtils.getStaticPath() + confFileUrl);
            if (confFile.exists()) {
                confFile.delete();
            }
            FILE_URL_MAP.remove(uploadFile.getIdentifier());
        }
    }

    @Override
    protected void doUploadFileChunk(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        // 本地存储不需要分片上传逻辑
    }

    @Override
    protected UploadFileResult organizationalResults(UploadMultipartFile uploadMultipartFile, UploadFile uploadFile) {
        // 本地存储不需要组织结果逻辑
        return null;
    }
}
