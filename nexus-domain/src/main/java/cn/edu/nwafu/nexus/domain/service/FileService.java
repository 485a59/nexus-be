package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.domain.response.FileDetailVo;
import cn.edu.nwafu.nexus.infrastructure.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Huang Z.Y.
 */
public interface FileService extends IService<File> {
    Long getFilePointCount(String fileId);

    void unzipFile(String userFileId, int unzipMode, String filePath);

    void updateFileDetail(String userFileId, String identifier, long fileSize);

    FileDetailVo getFileDetail(String userFileId);
}
