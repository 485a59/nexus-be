package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.entity.CommonFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface CommonFileService extends IService<CommonFile> {
    List<CommonFileUser> selectCommonFileUser(String userId);

    List<CommonFileListVo> selectCommonFileByUser(String userId, String sessionUserId);
}
