package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface UserFileMapper extends BaseMapper<UserFile> {
    List<UserFile> selectUserFileByLikeRightFilePath(@Param("filePath") String filePath, @Param("userId") String userId);

    List<FileListVo> selectPageVo(@Param("userFile") UserFile userFile, @Param("fileTypeId") Integer fileTypeId);

    Long selectStorageSizeByUserId(@Param("userId") String userId);
}
