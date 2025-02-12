package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.exception.ApiException;
import cn.edu.nwafu.nexus.domain.service.SysDeptService;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysDeptMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateDeptDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.DeptListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateDeptDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysDept;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptDetailVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptListVo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
@Slf4j
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<DeptListVo> list(DeptListDto query) {
        return sysDeptMapper.selectDeptList(query);
    }

    @Override
    public DeptDetailVo getInfo(Long deptId) {
        SysDept dept = sysDeptMapper.selectDeptById(deptId);
        if (dept == null) {
            throw new ApiException("部门不存在");
        }
        return BeanUtil.copyProperties(dept, DeptDetailVo.class);
    }

    @Override
    public List<Tree<Integer>> buildDeptTreeSelect() {
        List<SysDept> depts = sysDeptMapper.selectList(null);

        List<TreeNode<Integer>> nodeList = depts.stream().map(dept -> {
            TreeNode<Integer> node = new TreeNode<>();
            node.setId(dept.getId().intValue());
            node.setParentId(dept.getParentId().intValue());
            node.setName(dept.getName());
            node.setWeight(dept.getOrderNum());
            return node;
        }).collect(Collectors.toList());

        return TreeUtil.build(nodeList, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateDeptDto command) {
        // 校验部门名称是否唯一
        if (checkDeptNameUnique(command.getName(), command.getParentId())) {
            throw new ApiException("新增部门'" + command.getName() + "'失败，部门名称已存在");
        }

        SysDept dept = new SysDept();
        BeanUtil.copyProperties(command, dept);

        // 如果父节点不为0，需要更新ancestors
        if (!Objects.equals(command.getParentId(), 0L)) {
            SysDept parentDept = sysDeptMapper.selectDeptById(command.getParentId());
            if (parentDept == null) {
                throw new ApiException("父部门不存在");
            }
            dept.setAncestors(parentDept.getAncestors() + "," + parentDept.getId());
        } else {
            dept.setAncestors("0");
        }

        sysDeptMapper.insertDept(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long deptId, UpdateDeptDto command) {
        // 校验是否存在
        SysDept oldDept = sysDeptMapper.selectDeptById(deptId);
        if (oldDept == null) {
            throw new ApiException("部门不存在");
        }

        // 不能把部门的父节点设置为自己或自己的子节点
        if (Objects.equals(command.getParentId(), deptId)) {
            throw new ApiException("修改部门'" + command.getName() + "'失败，上级部门不能是自己");
        }
        if (hasChildByDeptId(deptId) && Objects.equals(command.getParentId(), deptId)) {
            throw new ApiException("修改部门'" + command.getName() + "'失败，上级部门不能是自己的子部门");
        }

        // 校验部门名称是否唯一
        if (checkDeptNameUnique(command.getName(), command.getParentId())) {
            throw new ApiException("修改部门'" + command.getName() + "'失败，部门名称已存在");
        }

        SysDept dept = new SysDept();
        dept.setId(deptId.intValue());
        BeanUtil.copyProperties(command, dept);

        sysDeptMapper.updateDept(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long deptId) {
        // 判断是否存在子部门
        if (hasChildByDeptId(deptId)) {
            throw new ApiException("存在下级部门,不允许删除");
        }

        sysDeptMapper.deleteDeptById(deptId);
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果 true-存在 false-不存在
     */
    private boolean checkDeptNameUnique(String deptName, Long parentId) {
        return sysDeptMapper.checkDeptNameUnique(deptName, parentId) > 0;
    }

    /**
     * 是否存在子部门
     *
     * @param deptId 部门ID
     * @return 结果 true-存在 false-不存在
     */
    private boolean hasChildByDeptId(Long deptId) {
        return sysDeptMapper.hasChildByDeptId(deptId) > 0;
    }
}
