package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.entity.Storage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Huang Z.Y.
 */
public interface StorageService extends IService<Storage> {
    Long getTotalStorageSize(String userId);

    boolean checkStorage(String userId, Long size);
}
