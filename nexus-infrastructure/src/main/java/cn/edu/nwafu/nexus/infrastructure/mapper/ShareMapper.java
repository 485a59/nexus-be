package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.entity.Share;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface ShareMapper extends BaseMapper<Share> {
    List<ShareListVo> selectShareList(String userId);

    int selectShareListTotalCount(String shareFilePath, String shareBatchNum, String userId);
}
