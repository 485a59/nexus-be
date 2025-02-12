package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.file.ShareListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Share;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 分享服务接口。
 *
 * @author Huang Z.Y.
 */
public interface ShareService extends IService<Share> {
    List<ShareListVo> selectShareList(String userId, Integer pageSize, Integer pageNum);

    int selectShareListTotalCount(ShareListDto shareListDto, String userId);
}
