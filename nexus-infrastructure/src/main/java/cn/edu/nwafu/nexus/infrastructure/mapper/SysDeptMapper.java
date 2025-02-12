package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.dto.system.DeptListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysDept;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    List<DeptListVo> selectDeptList(DeptListDto query);

    SysDept selectDeptById(@Param("deptId") Long deptId);

    int insertDept(SysDept dept);

    int updateDept(SysDept dept);

    int deleteDeptById(@Param("deptId") Long deptId);

    int hasChildByDeptId(@Param("deptId") Long deptId);

    int checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);
}
