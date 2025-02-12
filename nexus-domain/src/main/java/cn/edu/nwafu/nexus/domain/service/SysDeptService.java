package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateDeptDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.DeptListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateDeptDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptDetailVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptListVo;
import cn.hutool.core.lang.tree.Tree;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface SysDeptService {
    List<DeptListVo> list(DeptListDto query);

    DeptDetailVo getInfo(Long deptId);

    List<Tree<Integer>> buildDeptTreeSelect();

    void add(CreateDeptDto command);

    void update(Long deptId, UpdateDeptDto command);

    void remove(Long deptId);
}
