package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.CourseResourceService;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateSlideDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateSoftwareDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateTextbookDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.CreateVideoDto;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
