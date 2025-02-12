package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.FileTransferService;
import cn.edu.nwafu.nexus.domain.service.StorageService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.BatchDownloadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.DownloadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.PreviewDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Storage;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.UploadFileVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Api(tags = "文件传输 API")
@Tag(name = "文件传输 API", description = "该接口为文件传输接口，主要用来做文件的上传、下载和预览")
@RestController
@RequestMapping("/transfer")
public class FileTransferController {
    @Resource
    StorageService storageService;
    @Resource
    private FileTransferService fileTransferService;

    @Operation(summary = "极速上传")
    @GetMapping("/upload/check")
    public CommonResult<UploadFileVo> uploadFileSpeed(UploadFileDto uploadFileDto) {
        boolean isCheckSuccess = storageService.checkStorage(SessionUtils.getUserId(), uploadFileDto.getTotalSize());
        if (!isCheckSuccess) {
            return CommonResult.failed("存储空间不足");
        }
        UploadFileVo uploadFileVo = fileTransferService.uploadFileSpeed(uploadFileDto);
        return CommonResult.success(uploadFileVo);
    }

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public CommonResult<UploadFileVo> uploadFile(HttpServletRequest request, UploadFileDto uploadFileDto) {
        fileTransferService.uploadFile(request, uploadFileDto, SessionUtils.getUserId());
        return CommonResult.success(null);
    }

    @Operation(summary = "下载文件")
    @GetMapping("/download")
    public CommonResult<?> downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        DownloadFileDto downloadFileDto) {
        fileTransferService.handleFileDownload(httpServletRequest, httpServletResponse, downloadFileDto);
        return CommonResult.success(null);
    }

    @Operation(summary = "批量下载文件")
    @PostMapping("/download/batch")
    public CommonResult<?> batchDownloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                             @RequestBody BatchDownloadFileDto batchDownloadFileDto) {
        fileTransferService.handleBatchFileDownload(httpServletRequest, httpServletResponse, batchDownloadFileDto);
        return CommonResult.success(null);
    }

    @Operation(summary = "预览文件")
    @GetMapping("/preview")
    public CommonResult<?> preview(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                   PreviewDto previewDto) throws IOException {
        fileTransferService.handleFilePreview(httpServletRequest, httpServletResponse, previewDto);
        return CommonResult.success(null);
    }

    @Operation(summary = "获取存储信息")
    @GetMapping("/storage")
    public CommonResult<Storage> getStorage() {
        Storage storageBean = new Storage();
        storageBean.setUserId(SessionUtils.getUserId());
        Long storageSize = fileTransferService.selectStorageSizeByUserId(SessionUtils.getUserId());
        Storage storage = new Storage();
        storage.setUserId(SessionUtils.getUserId());
        storage.setStorageSize(storageSize);
        Long totalStorageSize = storageService.getTotalStorageSize(SessionUtils.getUserId());
        storage.setTotalStorageSize(totalStorageSize);
        return CommonResult.success(storage);
    }
}
