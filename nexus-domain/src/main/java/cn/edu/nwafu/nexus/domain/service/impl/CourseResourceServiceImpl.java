package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.constant.ResourceTypeEnums;
import cn.edu.nwafu.nexus.domain.service.CourseResourceService;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.infrastructure.mapper.CourseResourceMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateSlideDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateSoftwareDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateTextbookDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateVideoDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.CourseResource;
import cn.edu.nwafu.nexus.infrastructure.model.meta.SlideMeta;
import cn.edu.nwafu.nexus.infrastructure.model.meta.SoftwareMeta;
import cn.edu.nwafu.nexus.infrastructure.model.meta.TextbookMeta;
import cn.edu.nwafu.nexus.infrastructure.model.meta.VideoMeta;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Service
public class CourseResourceServiceImpl extends ServiceImpl<CourseResourceMapper, CourseResource> implements CourseResourceService {
    @Resource
    private CourseResourceMapper courseResourceMapper;
    @Resource
    private FileTransferService fileTransferService;

    @Override
    public void createTextbook(CreateTextbookDto command) {
        CourseResource courseResource = new CourseResource();
        courseResource.setType(ResourceTypeEnums.TEXTBOOK.getCode());
        courseResource.setDescription(command.getDescription());
        courseResource.setName(command.getName());
        courseResource.setFileId(command.getFileId());
        // 创建元数据
        TextbookMeta textbookMeta = new TextbookMeta();
        textbookMeta.setPublisher(command.getPublisher());
        textbookMeta.setPublishDate(command.getPublishDate());
        textbookMeta.setIsbn(command.getIsbn());
        textbookMeta.setAuthor(command.getAuthor());
        textbookMeta.setEdition(command.getEdition());
        String meta = JSON.toJSONString(textbookMeta);
        courseResource.setMeta(meta);

        save(courseResource);
    }

    @Override
    public void createVideo(CreateVideoDto command) {
        CourseResource courseResource = new CourseResource();
        courseResource.setType(ResourceTypeEnums.VIDEO.getCode());
        courseResource.setName(command.getName());
        courseResource.setChapterId(command.getChapterId());
        courseResource.setDescription(command.getDescription());
        courseResource.setFileId(command.getFileId());
        // 创建元数据
        VideoMeta videoMeta = new VideoMeta();
        videoMeta.setLecturer(command.getLecturer());
        videoMeta.setPoster(command.getPoster());
        String meta = JSON.toJSONString(videoMeta);
        courseResource.setMeta(meta);

        save(courseResource);
    }

    @Override
    public void createSlide(CreateSlideDto command) {
        CourseResource courseResource = new CourseResource();
        courseResource.setType(ResourceTypeEnums.SLIDE.getCode());
        courseResource.setName(command.getName());
        courseResource.setChapterId(command.getChapterId());
        courseResource.setDescription(command.getDescription());
        courseResource.setFileId(command.getFileId());
        // 创建元数据
        SlideMeta slideMeta = new SlideMeta();
        slideMeta.setAuthor(command.getAuthor());
        String meta = JSON.toJSONString(slideMeta);
        courseResource.setMeta(meta);

        save(courseResource);
    }

    @Override
    public void createSoftware(CreateSoftwareDto command) {
        CourseResource courseResource = new CourseResource();
        courseResource.setType(ResourceTypeEnums.SOFTWARE.getCode());
        courseResource.setName(command.getName());
        courseResource.setDescription(command.getDescription());
        courseResource.setFileId(command.getFileId());
        // 创建元数据
        SoftwareMeta softwareMeta = new SoftwareMeta();
        softwareMeta.setVersion(command.getVersion());
        softwareMeta.setDeveloper(command.getDeveloper());
        softwareMeta.setPlatform(command.getPlatform());
        softwareMeta.setRequirements(command.getRequirements());
        softwareMeta.setLicense(command.getLicense());
        String meta = JSON.toJSONString(softwareMeta);
        courseResource.setMeta(meta);

        save(courseResource);
    }
}
