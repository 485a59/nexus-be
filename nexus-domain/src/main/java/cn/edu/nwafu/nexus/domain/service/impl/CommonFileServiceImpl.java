package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.CommonFileService;
import cn.edu.nwafu.nexus.infrastructure.mapper.CommonFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.CommonFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Service
public class CommonFileServiceImpl extends ServiceImpl<CommonFileMapper, CommonFile> implements CommonFileService {
    @Resource
    private CommonFileMapper commonFileMapper;

    @Override
    public List<CommonFileUser> selectCommonFileUser(String userId) {
        return commonFileMapper.selectCommonFileUser(userId);
    }

    @Override
    public List<CommonFileListVo> selectCommonFileByUser(String userId, String sessionUserId) {
        return commonFileMapper.selectCommonFileByUser(userId, sessionUserId);
    }
}
