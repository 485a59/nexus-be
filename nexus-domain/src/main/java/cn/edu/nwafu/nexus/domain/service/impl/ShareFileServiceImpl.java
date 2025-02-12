package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.ShareFileService;
import cn.edu.nwafu.nexus.infrastructure.mapper.ShareFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.ShareFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareFileListVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Service
public class ShareFileServiceImpl extends ServiceImpl<ShareFileMapper, ShareFile> implements ShareFileService {
    @Resource
    private ShareFileMapper shareFileMapper;

    @Override
    public List<ShareFileListVo> selectShareFileList(String shareBatchNum, String filePath) {
        return shareFileMapper.selectShareFileList(shareBatchNum, filePath);
    }
}
