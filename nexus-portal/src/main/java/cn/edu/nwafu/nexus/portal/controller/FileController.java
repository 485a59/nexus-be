package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.response.FileDetailVo;
import cn.edu.nwafu.nexus.domain.service.FileApplicationService;
import cn.edu.nwafu.nexus.domain.service.FileService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.domain.util.TreeNode;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.*;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.SearchFileVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Api(tags = "文件操作 API")
@Tag(name = "文件操作 API", description = "文件基本操作，如创建、删除、移动、复制等")
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {
    @Resource
    private UFOPFactory ufopFactory;
    @Resource
    private FileHandler fileHandler;
    @Resource
    private UserFileService userFileService;
    @Resource
    private FileService fileService;
    @Resource
    private FileApplicationService fileApplicationService;
    @Value("${ufop.storage-type}")
    private Integer storageType;

    @Operation(summary = "创建文件")
    @PostMapping
    public CommonResult<String> createFile(@Valid @RequestBody CreateFileDto createFileDto) {
        fileApplicationService.createFile(createFileDto);
        return CommonResult.success("文件创建成功");
    }

    @Operation(summary = "创建文件夹")
    @PostMapping("/folder")
    public CommonResult<String> createFolder(@Valid @RequestBody CreateFolderDto createFoldDto) {
        fileApplicationService.createFolder(createFoldDto);
        return CommonResult.success("创建文件夹成功");
    }

    @Operation(summary = "文件搜索")
    @GetMapping("/search")
    public CommonResult<CommonPage<SearchFileVo>> searchFile(SearchFileDto searchFileDto) {
        List<SearchFileVo> list = fileApplicationService.searchFile(searchFileDto);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "文件重命名")
    @PutMapping("rename")
    public CommonResult<String> renameFile(@RequestBody RenameFileDto renameFileDto) {
        fileApplicationService.renameFile(renameFileDto);
        return CommonResult.success("文件重命名成功");
    }

    @Operation(summary = "获取文件列表")
    @PostMapping("/list")
    public CommonResult<CommonPage<FileListVo>> getFileList(@RequestBody FileListDto fileListDto,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Long pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum) {
        List<FileListVo> list = fileApplicationService.getFileList(fileListDto, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "批量删除文件")
    @DeleteMapping("/batch")
    public CommonResult<String> batchDeleteFiles(@RequestBody BatchDeleteFileDto batchDeleteFileDto) {
        fileApplicationService.batchDeleteFiles(batchDeleteFileDto);
        return CommonResult.success("批量删除文件成功");
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{id}")
    public CommonResult<?> deleteFile(@PathVariable String id) {
        userFileService.deleteUserFile(id, SessionUtils.getUserId());
        fileHandler.deleteESByUserFileId(id);
        return CommonResult.success("删除文件成功");
    }

    @Operation(summary = "解压文件")
    @PostMapping("/{id}/unzip")
    public CommonResult<String> unzipFile(@PathVariable String id, @RequestBody UnzipFileDto unzipFileDto) {
        try {
            fileService.unzipFile(id, unzipFileDto.getMode(), unzipFileDto.getPath());
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("解压文件成功");
    }

    @Operation(summary = "文件复制")
    @PostMapping("/copy")
    public CommonResult<String> copyFile(@RequestBody CopyFileDto copyFileDto) {
        fileApplicationService.copyFile(copyFileDto);
        return CommonResult.success("复制文件成功");
    }

    @Operation(summary = "文件移动")
    @PostMapping("/move")
    public CommonResult<String> moveFile(@RequestBody MoveFileDto moveFileDto) {
        fileApplicationService.moveFile(moveFileDto);
        return CommonResult.success("移动文件成功");
    }

    @Operation(summary = "批量移动文件")
    @PostMapping("/move/batch")
    public CommonResult<String> batchMoveFile(@RequestBody BatchMoveFileDto batchMoveFileDto) {
        fileApplicationService.batchMoveFile(batchMoveFileDto);
        return CommonResult.success("批量移动文件成功");
    }

    @Operation(summary = "获取文件树")
    @GetMapping("/tree")
    public CommonResult<TreeNode> getFileTree() {
        TreeNode treeNode = fileApplicationService.getFileTree();
        return CommonResult.success(treeNode);
    }

    @Operation(summary = "修改文件")
    @PutMapping
    public CommonResult<String> updateFile(@RequestBody UpdateFileDto updateFileDto) {
        fileApplicationService.updateFile(updateFileDto);
        return CommonResult.success("修改文件成功");
    }

    @Operation(summary = "查询文件详情")
    @GetMapping("/{id}")
    public CommonResult<FileDetailVo> getFileDetail(@PathVariable String id) {
        FileDetailVo vo = fileService.getFileDetail(id);
        return CommonResult.success(vo);
    }
}
