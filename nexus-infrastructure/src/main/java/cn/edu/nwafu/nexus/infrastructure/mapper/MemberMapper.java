package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层。
 *
 * @author Huang Z.Y.
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
