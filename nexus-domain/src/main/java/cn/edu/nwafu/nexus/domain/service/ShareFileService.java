package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.entity.ShareFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareFileListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 分享文件服务接口。
 *
 * @author Huang Z.Y.
 */
public interface ShareFileService extends IService<ShareFile> {
    List<ShareFileListVo> selectShareFileList(String shareBatchNum, String filePath);
}
