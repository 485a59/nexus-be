package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.StorageService;
import cn.edu.nwafu.nexus.infrastructure.mapper.StorageMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysParamMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Storage;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
    @Resource
    private SysParamMapper sysParamMapper;
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private StorageMapper storageMapper;

    public Long getTotalStorageSize(String userId) {
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Storage::getUserId, userId);

        Storage storageBean = storageMapper.selectOne(lambdaQueryWrapper);
        Long totalStorageSize = null;
        if (storageBean == null || storageBean.getTotalStorageSize() == null) {
            LambdaQueryWrapper<SysParam> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(SysParam::getKey, "totalStorageSize");
            SysParam sysParam = sysParamMapper.selectOne(lambdaQueryWrapper1);
            totalStorageSize = Long.parseLong(sysParam.getValue());
            storageBean = new Storage();
            storageBean.setUserId(userId);
            storageBean.setTotalStorageSize(totalStorageSize);
            storageMapper.insert(storageBean);
        } else {
            totalStorageSize = storageBean.getTotalStorageSize();
        }
        totalStorageSize = totalStorageSize * 1024 * 1024;
        return totalStorageSize;
    }

    public boolean checkStorage(String userId, Long fileSize) {
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Storage::getUserId, userId);

        Storage storageBean = storageMapper.selectOne(lambdaQueryWrapper);
        Long totalStorageSize = null;
        if (storageBean == null || storageBean.getTotalStorageSize() == null) {
            LambdaQueryWrapper<SysParam> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(SysParam::getKey, "totalStorageSize");
            SysParam sysParam = sysParamMapper.selectOne(lambdaQueryWrapper1);
            totalStorageSize = Long.parseLong(sysParam.getValue());
            storageBean = new Storage();
            storageBean.setUserId(userId);
            storageBean.setTotalStorageSize(totalStorageSize);
            storageMapper.insert(storageBean);
        } else {
            totalStorageSize = storageBean.getTotalStorageSize();
        }
        totalStorageSize = totalStorageSize * 1024 * 1024;

        Long storageSize = userFileMapper.selectStorageSizeByUserId(userId);
        if (storageSize == null) {
            storageSize = 0L;
        }
        return storageSize + fileSize <= totalStorageSize;
    }
}
