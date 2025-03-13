package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.constant.ResourceTypeEnums;
import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.service.CourseResourceService;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.infrastructure.mapper.ChapterMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.CourseResourceMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.FileMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.*;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Chapter;
import cn.edu.nwafu.nexus.infrastructure.model.entity.CourseResource;
import cn.edu.nwafu.nexus.infrastructure.model.entity.File;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.meta.SlideMeta;
import cn.edu.nwafu.nexus.infrastructure.model.meta.SoftwareMeta;
import cn.edu.nwafu.nexus.infrastructure.model.meta.TextbookMeta;
import cn.edu.nwafu.nexus.infrastructure.model.meta.VideoMeta;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.*;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private FileHandler fileHandler;
    @Value("${ufop.aliyun.oss.endpoint}")
    private String ossEndpoint;
    @Value("${ufop.aliyun.oss.bucket-name}")
    private String ossBucketName;

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
    public List<TextbookListVo> listTextbook(TextbookListDto command, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<CourseResource> wrapper = new QueryWrapper<>();
        wrapper.eq("type", ResourceTypeEnums.TEXTBOOK.getCode())
                .like(StrUtil.isNotEmpty(command.getName()), "name", command.getName())
                .like(StrUtil.isNotEmpty(command.getPublisher()), "meta->>'$.publisher'", command.getPublisher())
                .like(StrUtil.isNotEmpty(command.getAuthor()), "meta->>'$.author'", command.getAuthor())
                .orderByDesc("create_time");

        List<CourseResource> resources = list(wrapper);

        return resources.stream().map(resource -> {
            TextbookListVo vo = new TextbookListVo();
            vo.setId(resource.getId());
            vo.setName(resource.getName());
            vo.setFileId(resource.getFileId());

            setFileUrl(resource.getFileId(), vo);

            // 解析元数据
            TextbookMeta meta = JSON.parseObject(resource.getMeta(), TextbookMeta.class);
            if (meta != null) {
                vo.setPublisher(meta.getPublisher());
                vo.setIsbn(meta.getIsbn());
                vo.setAuthor(meta.getAuthor());
                vo.setEdition(meta.getEdition());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        CourseResource courseResource = courseResourceMapper.selectById(id);
        LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFile::getFileId, courseResource.getFileId());
        userFileMapper.delete(wrapper);
        fileMapper.deleteById(courseResource.getFileId());
        fileHandler.deleteESByUserFileId(id);
        courseResourceMapper.deleteById(id);
    }

    @Override
    public List<VideoTreeNodeVo> listVideoTree() {
        // 获取所有根章节（父 ID 为 0 的章节）
        LambdaQueryWrapper<Chapter> rootChapterWrapper = new LambdaQueryWrapper<>();
        rootChapterWrapper.eq(Chapter::getParentId, 0);
        List<Chapter> rootChapters = chapterMapper.selectList(rootChapterWrapper);

        return rootChapters.stream().map(this::buildVideoTreeNode).collect(Collectors.toList());
    }

    @Override
    public List<SlideTreeNodeVo> listSlideTree() {
        // 获取所有根章节（父 ID 为 0 的章节）
        LambdaQueryWrapper<Chapter> rootChapterWrapper = new LambdaQueryWrapper<>();
        rootChapterWrapper.eq(Chapter::getParentId, 0);
        List<Chapter> rootChapters = chapterMapper.selectList(rootChapterWrapper);

        return rootChapters.stream().map(this::buildSlideTreeNode).collect(Collectors.toList());
    }

    @Override
    public List<SoftwareTreeNodeVo> listSoftwareTree() {
        // 获取所有软件资源
        LambdaQueryWrapper<CourseResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseResource::getType, ResourceTypeEnums.SOFTWARE.getCode());
        List<CourseResource> allSoftware = list(wrapper);
        
        // 按分类组织软件
        Map<String, List<CourseResource>> softwareByCategory = allSoftware.stream()
                .collect(Collectors.groupingBy(software -> {
                    SoftwareMeta meta = JSON.parseObject(software.getMeta(), SoftwareMeta.class);
                    return meta != null ? meta.getCategory() : "其他";
                }));
        
        // 构建树形结构
        return softwareByCategory.entrySet().stream().map(entry -> {
            // 创建分类节点
            SoftwareTreeNodeVo categoryNode = new SoftwareTreeNodeVo();
            categoryNode.setId("");  // 分类节点 id 为空字符串
            categoryNode.setName(entry.getKey());
            
            // 创建软件节点列表
            List<SoftwareTreeNodeVo> softwareNodes = entry.getValue().stream().map(software -> {
                SoftwareTreeNodeVo softwareNode = new SoftwareTreeNodeVo();
                softwareNode.setId(software.getId());
                softwareNode.setName(software.getName());
                softwareNode.setDescription(software.getDescription());
                
                // 设置软件URL
                setFileUrl(software.getFileId(), softwareNode);
                
                // 获取文件大小和更新时间
                File file = fileMapper.selectById(software.getFileId());
                if (file != null) {
                    softwareNode.setSize(file.getSize());
                    softwareNode.setUpdateTime(file.getUpdateTime());
                }
                
                // 解析软件元数据
                SoftwareMeta meta = JSON.parseObject(software.getMeta(), SoftwareMeta.class);
                if (meta != null) {
                    softwareNode.setVersion(meta.getVersion());
                    softwareNode.setPlatform(meta.getPlatform());
                }
                
                return softwareNode;
            }).toList();
            
            categoryNode.setChildren(softwareNodes);
            return categoryNode;
        }).toList();
    }

    private VideoTreeNodeVo buildVideoTreeNode(Chapter chapter) {
        VideoTreeNodeVo node = new VideoTreeNodeVo();
        node.setId(String.valueOf(chapter.getId()));
        node.setName(chapter.getName());

        // 获取子章节
        LambdaQueryWrapper<Chapter> childChapterWrapper = new LambdaQueryWrapper<>();
        childChapterWrapper.eq(Chapter::getParentId, chapter.getId());
        List<Chapter> childChapters = chapterMapper.selectList(childChapterWrapper);

        // 获取当前章节的视频资源
        LambdaQueryWrapper<CourseResource> videoWrapper = new LambdaQueryWrapper<>();
        videoWrapper.eq(CourseResource::getChapterId, chapter.getId())
                .eq(CourseResource::getType, ResourceTypeEnums.VIDEO.getCode());
        List<CourseResource> videos = list(videoWrapper);

        // 构建子节点列表
        List<VideoTreeNodeVo> children = new ArrayList<>();

        // 添加子章节节点
        if (!childChapters.isEmpty()) {
            children.addAll(childChapters.stream()
                    .map(this::buildVideoTreeNode)
                    .toList());
        }

        // 添加视频节点
        if (!videos.isEmpty()) {
            children.addAll(videos.stream().map(video -> {
                VideoTreeNodeVo videoNode = new VideoTreeNodeVo();
                videoNode.setId(video.getId());
                videoNode.setName(video.getName());
                videoNode.setDescription(video.getDescription());

                // 设置视频URL
                setFileUrl(video.getFileId(), videoNode);

                // 解析视频元数据
                VideoMeta meta = JSON.parseObject(video.getMeta(), VideoMeta.class);
                if (meta != null) {
                    videoNode.setLecturer(meta.getLecturer());
                }

                return videoNode;
            }).toList());
        }

        node.setChildren(children);
        return node;
    }

    private SlideTreeNodeVo buildSlideTreeNode(Chapter chapter) {
        SlideTreeNodeVo node = new SlideTreeNodeVo();
        node.setId(String.valueOf(chapter.getId()));
        node.setName(chapter.getName());

        // 获取子章节
        LambdaQueryWrapper<Chapter> childChapterWrapper = new LambdaQueryWrapper<>();
        childChapterWrapper.eq(Chapter::getParentId, chapter.getId());
        List<Chapter> childChapters = chapterMapper.selectList(childChapterWrapper);

        // 获取当前章节的幻灯片资源
        LambdaQueryWrapper<CourseResource> slideWrapper = new LambdaQueryWrapper<>();
        slideWrapper.eq(CourseResource::getChapterId, chapter.getId())
                .eq(CourseResource::getType, ResourceTypeEnums.SLIDE.getCode());
        List<CourseResource> slides = list(slideWrapper);

        // 构建子节点列表
        List<SlideTreeNodeVo> children = new ArrayList<>();

        // 添加子章节节点
        if (!childChapters.isEmpty()) {
            children.addAll(childChapters.stream()
                    .map(this::buildSlideTreeNode)
                    .toList());
        }

        // 添加幻灯片节点
        if (!slides.isEmpty()) {
            children.addAll(slides.stream().map(slide -> {
                SlideTreeNodeVo slideNode = new SlideTreeNodeVo();
                slideNode.setId(slide.getId());
                slideNode.setName(slide.getName());
                slideNode.setDescription(slide.getDescription());

                // 设置幻灯片URL
                setFileUrl(slide.getFileId(), slideNode);

                // 解析幻灯片元数据
                SlideMeta meta = JSON.parseObject(slide.getMeta(), SlideMeta.class);
                if (meta != null) {
                    slideNode.setAuthor(meta.getAuthor());
                }

                return slideNode;
            }).toList());
        }

        node.setChildren(children);
        return node;
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
        String meta = JSON.toJSONString(videoMeta);
        courseResource.setMeta(meta);

        save(courseResource);
    }

    @Override
    public List<VideoListVo> listVideo(VideoListDto command, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<CourseResource> wrapper = new QueryWrapper<>();
        wrapper.eq("type", ResourceTypeEnums.VIDEO.getCode())
                .like(StrUtil.isNotEmpty(command.getName()), "name", command.getName())
                .like(StrUtil.isNotEmpty(command.getLecturer()), "meta->>'$.lecturer'", command.getLecturer())
                .orderByDesc("create_time");

        List<CourseResource> resources = list(wrapper);

        return resources.stream().map(resource -> {
            VideoListVo vo = new VideoListVo();
            vo.setId(resource.getId());
            vo.setFileId(resource.getFileId());
            vo.setName(resource.getName());
            vo.setChapterId(resource.getChapterId());

            setFileUrl(resource.getFileId(), vo);

            // 查询章节名称
            Chapter chapter = chapterMapper.selectById(resource.getChapterId());
            if (chapter != null) {
                vo.setChapterName(chapter.getName());
                vo.setParentChapterId(chapter.getParentId());
            }

            // 解析元数据
            VideoMeta meta = JSON.parseObject(resource.getMeta(), VideoMeta.class);
            if (meta != null) {
                vo.setLecturer(meta.getLecturer());
            }

            return vo;
        }).collect(Collectors.toList());
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
    public List<SlideListVo> listSlide(SlideListDto command, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<CourseResource> wrapper = new QueryWrapper<>();
        wrapper.eq("type", ResourceTypeEnums.SLIDE.getCode())
                .like(StrUtil.isNotEmpty(command.getName()), "name", command.getName())
                .inSql(StrUtil.isNotEmpty(command.getChapterName()),
                        "chapter_id",
                        "SELECT id FROM chapter WHERE name LIKE '%" + command.getChapterName() + "%'")
                .orderByDesc("create_time");

        List<CourseResource> resources = list(wrapper);

        return resources.stream().map(resource -> {
            SlideListVo vo = new SlideListVo();
            vo.setId(resource.getId());
            vo.setName(resource.getName());
            vo.setFileId(resource.getFileId());
            vo.setDescription(resource.getDescription());

            setFileUrl(resource.getFileId(), vo);

            // 查询章节名称
            Chapter chapter = chapterMapper.selectById(resource.getChapterId());
            if (chapter != null) {
                vo.setChapterId(Integer.valueOf(chapter.getId()));
                vo.setChapterName(chapter.getName());
            }

            // 解析元数据
            SlideMeta meta = JSON.parseObject(resource.getMeta(), SlideMeta.class);
            if (meta != null) {
                vo.setAuthor(meta.getAuthor());
            }

            return vo;
        }).collect(Collectors.toList());
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
        softwareMeta.setPlatform(command.getPlatform());
        softwareMeta.setCategory(command.getCategory());
        String meta = JSON.toJSONString(softwareMeta);
        courseResource.setMeta(meta);

        save(courseResource);
    }

    @Override
    public List<SoftwareListVo> listSoftware(SoftwareListDto command, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<CourseResource> wrapper = new QueryWrapper<>();
        wrapper.eq("type", ResourceTypeEnums.SOFTWARE.getCode())
                .like(StrUtil.isNotEmpty(command.getName()), "name", command.getName())
                .like(StrUtil.isNotEmpty(command.getCategory()), "meta->>'$.category'", command.getCategory())
                .orderByDesc("create_time");

        List<CourseResource> resources = list(wrapper);

        return resources.stream().map(resource -> {
            SoftwareListVo vo = new SoftwareListVo();
            vo.setId(resource.getId());
            vo.setName(resource.getName());
            vo.setFileId(resource.getFileId());

            setFileUrl(resource.getFileId(), vo);

            // 解析元数据
            SoftwareMeta meta = JSON.parseObject(resource.getMeta(), SoftwareMeta.class);
            if (meta != null) {
                vo.setVersion(meta.getVersion());
                vo.setPlatform(meta.getPlatform());
                vo.setCategory(meta.getCategory());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    private String getOssEndpoint() {
        return "https://" + ossBucketName + "." + ossEndpoint;
    }

    /**
     * 设置文件URL
     *
     * @param fileId 文件ID
     * @param vo     需要设置URL的VO对象
     * @param <T>    VO类型
     */
    private <T> void setFileUrl(String fileId, T vo) {
        File file = fileMapper.selectById(fileId);
        if (file != null) {
            try {
                java.lang.reflect.Method setUrl = vo.getClass().getMethod("setUrl", String.class);
                setUrl.invoke(vo, getOssEndpoint() + SystemFile.separator + file.getUrl());
            } catch (Exception e) {
                log.error("设置文件URL失败", e);
            }
        }
    }
}
