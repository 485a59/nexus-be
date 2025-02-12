package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.file.BatchDownloadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.DownloadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.PreviewDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.File;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.UploadFileVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface FileTransferService {
    UploadFileVo uploadFileSpeed(UploadFileDto uploadFileDto);

    void uploadFile(HttpServletRequest request, UploadFileDto UploadFileDto, String userId);

    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDto downloadFileDto);

    void downloadUserFileList(HttpServletResponse httpServletResponse, String filePath, String fileName, List<String> userFileIds);

    void previewFile(HttpServletResponse httpServletResponse, PreviewDto previewDTO);

    void previewPictureFile(HttpServletResponse httpServletResponse, PreviewDto previewDto);

    void deleteFile(File file);

    Long selectStorageSizeByUserId(String userId);

    void handleFileDownload(HttpServletRequest request, HttpServletResponse response, DownloadFileDto downloadFileDto);
    
    void handleBatchFileDownload(HttpServletRequest request, HttpServletResponse response, 
                               BatchDownloadFileDto batchDownloadFileDto);
    
    void handleFilePreview(HttpServletRequest request, HttpServletResponse response, PreviewDto previewDto) 
            throws IOException;

    /**
     * 检查文件是否上传完成
     * @param identifier 文件唯一标识
     * @return 如果上传完成返回userFileId，否则返回null
     */
    String isFileUploadComplete(String identifier);
}
