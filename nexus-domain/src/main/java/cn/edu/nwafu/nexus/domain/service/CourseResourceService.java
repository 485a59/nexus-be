package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateSlideDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateSoftwareDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateTextbookDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateVideoDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.CourseResource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Huang Z.Y.
 */
public interface CourseResourceService extends IService<CourseResource> {
    /**
     * 创建教材资源。
     */
    void createTextbook(CreateTextbookDto command);

    /**
     * 创建视频资源。
     */
    void createVideo(CreateVideoDto command);

    /**
     * 创建幻灯片资源。
     */
    void createSlide(CreateSlideDto command);

    /**
     * 创建软件资源。
     */
    void createSoftware(CreateSoftwareDto command);
}
