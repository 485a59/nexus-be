package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.entity.RecoveryFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.RecoveryFileListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface RecoveryFileService extends IService<RecoveryFile> {
    void deleteUserFileByDeleteBatchNum(String deleteBatchNum);

    void restoreFile(String deleteBatchNum, String filePath, String sessionUserId);

    List<RecoveryFileListVo> selectRecoveryFileList(String userId, Integer pageSize, Integer pageNum);
}
