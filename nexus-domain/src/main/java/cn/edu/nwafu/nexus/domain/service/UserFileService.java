package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface UserFileService extends IService<UserFile> {
    List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, String userId);

    List<UserFile> selectSameUserFile(String fileName, String filePath, String extendName, String userId);

    List<FileListVo> userFileList(String userId, String filePath, Long beginCount, Long pageCount);

    void updateFilepathByUserFileId(String userFileId, String newFilePath, String userId);

    void userFileCopy(String userId, String userFileId, String newFilePath);

    List<FileListVo> getFileByFileType(Integer fileTypeId, Long currentPage, Long pageCount, String userId);

    List<UserFile> selectUserFileListByPath(String filePath, String userId);

    List<UserFile> selectFilePathTreeByUserId(String userId);

    void deleteUserFile(String userFileId, String sessionUserId);

    List<UserFile> selectUserFileByLikeRightFilePath(@Param("filePath") String filePath, @Param("userId") String userId);
}
