package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.CourseResourceService;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.*;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.SlideListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.SoftwareListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.TextbookListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.VideoListVo;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Api(tags = "课程资源 API")
@Tag(name = "课程资源 API", description = "此接口主要用于对课程资源进行全面管理操作")
@RequestMapping("/system/resource")
@RestController
public class SysResourceController {
    @Resource
    private CourseResourceService resourceService;
    @Resource
    private FileTransferService fileTransferService;

    @Operation(summary = "创建教材")
    @PostMapping("/textbook")
    public CommonResult<Void> createTextbook(@RequestBody CreateTextbookDto command) {
        // 验证文件是否上传完成
        String userFileId = fileTransferService.isFileUploadComplete(command.getIdentifier());
        if (StrUtil.isEmpty(userFileId)) {
            return CommonResult.failed("文件尚未上传完成");
        }
        command.setFileId(userFileId);
        // 创建教材元数据
        resourceService.createTextbook(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "创建视频")
    @PostMapping("/video")
    public CommonResult<Void> createVideo(@RequestBody CreateVideoDto command) {
        // 验证文件是否上传完成
        String userFileId = fileTransferService.isFileUploadComplete(command.getIdentifier());
        if (StrUtil.isEmpty(userFileId)) {
            return CommonResult.failed("文件尚未上传完成");
        }
        command.setFileId(userFileId);
        // 创建教材元数据
        resourceService.createVideo(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "创建幻灯片")
    @PostMapping("/slide")
    public CommonResult<Void> createVideo(@RequestBody CreateSlideDto command) {
        // 验证文件是否上传完成
        String userFileId = fileTransferService.isFileUploadComplete(command.getIdentifier());
        if (StrUtil.isEmpty(userFileId)) {
            return CommonResult.failed("文件尚未上传完成");
        }
        command.setFileId(userFileId);
        // 创建教材元数据
        resourceService.createSlide(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "幻灯片列表")
    @PostMapping("/slide/list")
    public CommonResult<CommonPage<SlideListVo>> listSlide(@RequestBody SlideListDto command,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SlideListVo> list = resourceService.listSlide(command, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "创建软件")
    @PostMapping("/software")
    public CommonResult<Void> createSoftware(@RequestBody CreateSoftwareDto command) {
        // 验证文件是否上传完成
        String userFileId = fileTransferService.isFileUploadComplete(command.getIdentifier());
        if (StrUtil.isEmpty(userFileId)) {
            return CommonResult.failed("文件尚未上传完成");
        }
        command.setFileId(userFileId);
        // 创建教材元数据
        resourceService.createSoftware(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "教材资源列表")
    @PostMapping("/textbook/list")
    public CommonResult<CommonPage<TextbookListVo>> listTextbook(@RequestBody TextbookListDto command,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<TextbookListVo> list = resourceService.listTextbook(command, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "视频资源列表")
    @PostMapping("/video/list")
    public CommonResult<CommonPage<VideoListVo>> listVideo(@RequestBody VideoListDto command,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<VideoListVo> list = resourceService.listVideo(command, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "软件资源列表")
    @PostMapping("/software/list")
    public CommonResult<CommonPage<SoftwareListVo>> listSoftware(@RequestBody SoftwareListDto command,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SoftwareListVo> list = resourceService.listSoftware(command, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "删除教材资源")
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable("id") String id) {
        resourceService.delete(id);
        return CommonResult.success(null);
    }
}
