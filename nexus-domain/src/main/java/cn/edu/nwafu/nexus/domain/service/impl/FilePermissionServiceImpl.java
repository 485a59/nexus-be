package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.FilePermissionService;
import cn.edu.nwafu.nexus.infrastructure.mapper.FilePermissionMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.FilePermission;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Huang Z.Y.
 */
@Service
public class FilePermissionServiceImpl extends ServiceImpl<FilePermissionMapper, FilePermission> implements FilePermissionService {
}
