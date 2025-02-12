package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.component.AsyncTaskHandler;
import cn.edu.nwafu.nexus.domain.service.FileService;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.domain.service.RecoveryFileService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.recycles.BatchDeleteRecoveryFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.recycles.RestoreFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.RecoveryFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.RecoveryFileListVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
@Api(tags = "回收站 API")
@Tag(name = "回收站 API", description = "文件删除后会进入回收站，该接口主要是对回收站文件进行管理")
@RestController
@Slf4j
@RequestMapping("/recycle")
public class RecoveryFileController {
    @Resource
    private FileService fileService;
    @Resource
    private FileTransferService filetransferService;
    @Resource
    private AsyncTaskHandler asyncTaskHandler;
    @Resource
    private RecoveryFileService recoveryFileService;
    @Resource
    private UserFileService userFileService;

    @Operation(summary = "删除回收文件")
    @DeleteMapping("/{id}")
    public CommonResult<String> deleteRecoveryFile(@PathVariable String id) {
        RecoveryFile recoveryFile = recoveryFileService.getOne(new QueryWrapper<RecoveryFile>().lambda()
                .eq(RecoveryFile::getId, id));
        if (recoveryFile != null) {
            asyncTaskHandler.deleteUserFile(recoveryFile.getUserFileId());
            recoveryFileService.removeById(recoveryFile.getId());
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("文件不存在");
    }

    @Operation(summary = "批量删除回收文件")
    @DeleteMapping("/batch")
    public CommonResult<String> batchDeleteRecoveryFile(@RequestBody BatchDeleteRecoveryFileDto batchDeleteRecoveryFileDto) {
        List<String> idList = batchDeleteRecoveryFileDto.getIds();
        for (String id : idList) {
            RecoveryFile recoveryFile = recoveryFileService.getOne(new QueryWrapper<RecoveryFile>().lambda()
                    .eq(RecoveryFile::getId, id));
            if (recoveryFile != null) {
                asyncTaskHandler.deleteUserFile(recoveryFile.getUserFileId());
                recoveryFileService.removeById(recoveryFile.getId());
            }
        }
        return CommonResult.success("批量删除成功");
    }

    @Operation(summary = "回收文件列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<RecoveryFileListVo>> getRecoveryFileList(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<RecoveryFileListVo> recoveryFileList = recoveryFileService.selectRecoveryFileList(SessionUtils.getUserId(),
                pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(recoveryFileList));
    }

    @Operation(summary = "还原文件")
    @PostMapping("/restore")
    public CommonResult<String> restoreFile(@RequestBody RestoreFileDto restoreFileDto) {
        recoveryFileService.restoreFile(restoreFileDto.getDeleteBatchNum(), restoreFileDto.getPath(),
                SessionUtils.getUserId());
        return CommonResult.success("还原成功");
    }
}
