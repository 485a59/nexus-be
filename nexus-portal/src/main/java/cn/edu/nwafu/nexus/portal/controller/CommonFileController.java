package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.service.CommonFileService;
import cn.edu.nwafu.nexus.domain.service.FilePermissionService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.commonfile.CommonFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.CommonFile;
import cn.edu.nwafu.nexus.infrastructure.model.entity.FilePermission;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile.CommonFileUser;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Api(tags = "共享文件 API")
@Tag(name = "共享文件 API", description = "文件共享相关的操作")
@RestController
@Slf4j
@RequestMapping("/public")
public class CommonFileController {
    @Resource
    private CommonFileService commonFileService;
    @Resource
    private FilePermissionService filePermissionService;
    @Resource
    private UserFileService userFileService;

    @Operation(summary = "创建共享文件")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<String> createShare(@RequestBody CommonFileDto commonFileDto) {
        CommonFile commonFile = new CommonFile();
        commonFile.setUserFileId(commonFileDto.getId());
        commonFileService.save(commonFile);
        List<FilePermission> list = commonFileDto.getPermissions();
        List<FilePermission> filePermissionList = new ArrayList<>();
        for (FilePermission filePermission : list) {
            filePermission.setId(IdUtil.getSnowflakeNextId());
            filePermission.setCommonFileId(commonFile.getId());
            filePermissionList.add(filePermission);
        }
        filePermissionService.saveBatch(filePermissionList);
        return CommonResult.success(null);
    }

    @Operation(summary = "共享空间用户列表")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public CommonResult<CommonPage<CommonFileUser>> listShareUsers() {
        String currentUserId = SessionUtils.getUserId();
        List<CommonFileUser> list = commonFileService.selectCommonFileUser(currentUserId);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "获取用户共享文件列表")
    @RequestMapping(value = "/list/user", method = RequestMethod.GET)
    public CommonResult<CommonPage<CommonFileListVo>> listUserShares(
            @Parameter(description = "用户ID", required = true) String userId) {
        String currentUserId = SessionUtils.getUserId();
        List<CommonFileListVo> shareFiles = commonFileService.selectCommonFileByUser(userId, currentUserId);
        return CommonResult.success(CommonPage.restPage(shareFiles));
    }

    @Operation(summary = "获取共享空间文件列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<FileListVo>> listShareFiles(
            @Parameter(description = "共享文件 ID", required = true) Long id,
            @Parameter(description = "文件路径", required = true) String path,
            @RequestParam(value = "pageSize", defaultValue = "5") Long pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum) {
        CommonFile commonFile = commonFileService.getById(id);
        UserFile userFile = userFileService.getById(commonFile.getUserFileId());
        SystemFile systemFile = new SystemFile(userFile.getFilePath(), path, true);
        List<FileListVo> fileList = userFileService.userFileList(userFile.getUserId(), systemFile.getPath(),
                pageNum, pageSize);

        return CommonResult.success(CommonPage.restPage(fileList));
    }
}
