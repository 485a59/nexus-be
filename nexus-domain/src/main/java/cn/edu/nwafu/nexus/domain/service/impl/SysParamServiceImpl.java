package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.SysParamService;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysParamMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysParam;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Huang Z.Y.
 */
@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam> implements SysParamService {
}
