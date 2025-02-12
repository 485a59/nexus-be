package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.ShareService;
import cn.edu.nwafu.nexus.infrastructure.mapper.ShareMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.ShareListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Share;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareListVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分享服务实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {
    @Resource
    private ShareMapper shareMapper;

    @Override
    public List<ShareListVo> selectShareList(String userId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return shareMapper.selectShareList(userId);
    }

    @Override
    public int selectShareListTotalCount(ShareListDto shareListDto, String userId) {
        return shareMapper.selectShareListTotalCount(shareListDto.getPath(), shareListDto.getShareBatchNum(), userId);
    }
}
