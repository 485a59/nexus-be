package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.ChapterListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Chapter;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.ChapterListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface ChapterMapper extends BaseMapper<Chapter> {
    List<ChapterListVo> selectChapterList(ChapterListDto chapterListDto);
}
