package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.ChapterListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Chapter;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.ChapterListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface ChapterService extends IService<Chapter> {
    List<ChapterListVo> list(ChapterListDto chapterListDto);
}
