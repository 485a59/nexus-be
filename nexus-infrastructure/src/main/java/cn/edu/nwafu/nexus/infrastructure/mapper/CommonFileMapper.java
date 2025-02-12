package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.entity.CommonFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface CommonFileMapper extends BaseMapper<CommonFile> {
    List<CommonFileUser> selectCommonFileUser(@Param("userId") String userId);

    List<CommonFileListVo> selectCommonFileByUser(@Param("userId") String userId, @Param("sessionUserId") String sessionUserId);
}
