package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.*;
import cn.edu.nwafu.nexus.infrastructure.model.entity.CourseResource;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface CourseResourceService extends IService<CourseResource> {
    /**
     * 创建教材资源。
     */
    void createTextbook(CreateTextbookDto command);

    /**
     * 教材资源列表。
     */
    List<TextbookListVo> listTextbook(TextbookListDto command, Integer pageSize, Integer pageNum);

    /**
     * 创建视频资源。
     */
    void createVideo(CreateVideoDto command);

    /**
     * 视频资源列表。
     */
    List<VideoListVo> listVideo(VideoListDto command, Integer pageSize, Integer pageNum);

    /**
     * 创建幻灯片资源。
     */
    void createSlide(CreateSlideDto command);

    /**
     * 幻灯片资源列表。
     */
    List<SlideListVo> listSlide(SlideListDto command, Integer pageSize, Integer pageNum);

    /**
     * 创建软件资源。
     */
    void createSoftware(CreateSoftwareDto command);

    /**
     * 软件资源列表。
     */
    List<SoftwareListVo> listSoftware(SoftwareListDto command, Integer pageSize, Integer pageNum);

    /**
     * 删除资源（通用）。
     */
    void delete(String id);

    /**
     * 视频资源树结构。
     */
    List<VideoTreeNodeVo> listVideoTree();

    /**
     * 幻灯片资源树。
     */
    List<SlideTreeNodeVo> listSlideTree();

    /**
     * 软件资源树。
     */
    List<SoftwareTreeNodeVo> listSoftwareTree();
}
