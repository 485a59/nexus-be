package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.ChapterService;
import cn.edu.nwafu.nexus.infrastructure.mapper.ChapterMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.ChapterListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Chapter;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.ChapterListVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Resource
    private ChapterMapper chapterMapper;

    @Override
    public List<ChapterListVo> list(ChapterListDto chapterListDto) {
        return chapterMapper.selectChapterList(chapterListDto);
    }
}
