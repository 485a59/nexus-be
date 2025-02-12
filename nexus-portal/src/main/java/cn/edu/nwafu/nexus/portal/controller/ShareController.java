package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.service.ShareFileService;
import cn.edu.nwafu.nexus.domain.service.ShareService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.ShareListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.share.SaveShareFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.share.ShareFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Share;
import cn.edu.nwafu.nexus.infrastructure.model.entity.ShareFile;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareFileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareFileVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.share.ShareTypeVo;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Huang Z.Y.
 */
@Api(tags = "文件分享 API")
@Tag(name = "文件分享 API", description = "该接口为文件分享接口")
@RestController
@Slf4j
@RequestMapping("/share")
public class ShareController {
    @Resource
    UserFileService userFileService;
    @Resource
    private FileHandler fileHandler;
    @Resource
    private ShareService shareService;
    @Resource
    private ShareFileService shareFileService;

    @Operation(summary = "分享文件")
    @PostMapping
    public CommonResult<ShareFileVo> shareFile(@RequestBody ShareFileDto shareFileDto) {
        ShareFileVo shareSecretVo = new ShareFileVo();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Share share = new Share();
        BeanUtil.copyProperties(shareFileDto, share);
        share.setShareTime(new Date());
        share.setUserId(SessionUtils.getUserId());
        share.setShareStatus(0);
        if (shareFileDto.getShareType() == 1) {
            share.setExtractionCode(shareFileDto.getExtractionCode());
            shareSecretVo.setExtractionCode(share.getExtractionCode());
        }
        share.setShareBatchNum(uuid);
        shareService.save(share);
        List<ShareFile> saveFileList = new ArrayList<>();
        List<String> userFileIds = shareFileDto.getIds();
        for (String userFileId : userFileIds) {
            UserFile userFile = userFileService.getById(userFileId);
            if (userFile.getUserId().compareTo(Objects.requireNonNull(SessionUtils.getUserId())) != 0) {
                return CommonResult.failed("您只能分享自己的文件");
            }
            if (userFile.getIsDir() == 1) {
                SystemFile systemFile = new SystemFile(userFile.getFilePath(), userFile.getFileName(), true);
                List<UserFile> userfileList = userFileService.selectUserFileByLikeRightFilePath(systemFile.getPath(),
                        SessionUtils.getUserId());
                for (UserFile userFile1 : userfileList) {
                    ShareFile shareFile1 = new ShareFile();
                    shareFile1.setUserFileId(userFile1.getId());
                    shareFile1.setShareBatchNum(uuid);
                    shareFile1.setShareFilePath(userFile1.getFilePath().replaceFirst(userFile.getFilePath().equals("/") ? "" : userFile.getFilePath(), ""));
                    saveFileList.add(shareFile1);
                }
            }
            ShareFile shareFile = new ShareFile();
            shareFile.setUserFileId(userFileId);
            shareFile.setShareFilePath("/");
            shareFile.setShareBatchNum(uuid);
            saveFileList.add(shareFile);
        }
        shareFileService.saveBatch(saveFileList);
        shareSecretVo.setShareBatchNum(uuid);
        return CommonResult.success(shareSecretVo);
    }

    @Operation(summary = "保存分享文件")
    @PostMapping("/{shareBatchNum}/save")
    public CommonResult<?> saveShareFile(@PathVariable String shareBatchNum,
                                         @RequestBody SaveShareFileDto saveShareFileDto) {
        String saveFilePath = saveShareFileDto.getPath();
        String userId = SessionUtils.getUserId();
        List<String> userFileIds = saveShareFileDto.getIds();
        List<UserFile> saveUserFileList = new ArrayList<>();
        for (String userFileId : userFileIds) {
            UserFile userFile = userFileService.getById(userFileId);
            String fileName = userFile.getFileName();
            String filePath = userFile.getFilePath();
            UserFile userFile2 = new UserFile();
            BeanUtil.copyProperties(userFile, userFile2);
            String saveFileName = fileHandler.getRepeatFileName(userFile, saveFilePath);
            if (userFile.getIsDir() == 1) {
                ShareFile shareFile = shareFileService.getOne(new QueryWrapper<ShareFile>().lambda().eq(ShareFile::getUserFileId, userFileId).eq(ShareFile::getShareBatchNum, shareBatchNum));
                List<ShareFile> shareFileList = shareFileService.list(new QueryWrapper<ShareFile>().lambda().eq(ShareFile::getShareBatchNum, shareBatchNum)
                        .likeRight(ShareFile::getShareFilePath, SystemFile.formatPath(shareFile.getShareFilePath() + "/" + fileName)));
                for (ShareFile shareFile1 : shareFileList) {
                    UserFile userFile1 = userFileService.getById(shareFile1.getUserFileId());
                    userFile1.setUserId(userId);
                    userFile1.setFilePath(userFile1.getFilePath()
                            .replaceFirst(SystemFile.formatPath(filePath + "/" + fileName), SystemFile.formatPath(saveFilePath + "/" + saveFileName)));
                    saveUserFileList.add(userFile1);
                    log.info("当前文件：{}", JSON.toJSONString(userFile1));
                }
            }
            userFile2.setUserId(userId);
            userFile2.setFilePath(saveFilePath);
            userFile2.setFileName(saveFileName);
            saveUserFileList.add(userFile2);
        }
        log.info(JSON.toJSONString(saveUserFileList));
        userFileService.saveBatch(saveUserFileList);
        return CommonResult.success(null);
    }

    @Operation(summary = "查看已分享列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<ShareListVo>> shareList(ShareListDto shareListDto,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<ShareListVo> shareList = shareService.selectShareList(SessionUtils.getUserId(), pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(shareList));
    }

    @Operation(summary = "分享文件列表")
    @GetMapping("/{shareBatchNum}/files")
    public CommonResult<CommonPage<ShareFileListVo>> shareFileList(@PathVariable String shareBatchNum,
                                                                   @RequestParam(required = false) String path) {
        List<ShareFileListVo> list = shareFileService.selectShareFileList(shareBatchNum, path);
        for (ShareFileListVo shareFileListVo : list) {
            shareFileListVo.setShareFilePath(path);
        }
        return CommonResult.success(CommonPage.restPage(list));
    }

    @Operation(summary = "获取分享类型")
    @GetMapping("/{shareBatchNum}/type")
    public CommonResult<ShareTypeVo> shareType(@PathVariable String shareBatchNum) {
        Share share = shareService.getOne(new LambdaQueryWrapper<Share>()
                .eq(Share::getShareBatchNum, shareBatchNum));
        ShareTypeVo shareTypeVo = new ShareTypeVo();
        shareTypeVo.setShareType(share.getShareType());
        return CommonResult.success(shareTypeVo);
    }

    @Operation(summary = "校验提取码")
    @PostMapping("/{shareBatchNum}/verify")
    public CommonResult<String> checkExtractionCode(@PathVariable String shareBatchNum,
                                                    @RequestBody String extractionCode) {
        Share share = shareService.getOne(new LambdaQueryWrapper<Share>()
                .eq(Share::getShareBatchNum, shareBatchNum)
                .eq(Share::getExtractionCode, extractionCode));
        return share != null ? CommonResult.success(null) : CommonResult.failed("校验失败");
    }

    @Operation(summary = "校验过期时间")
    @GetMapping("/{shareBatchNum}/validity")
    public CommonResult<String> checkEndTime(@PathVariable String shareBatchNum) {
        Share share = shareService.getOne(new LambdaQueryWrapper<Share>()
                .eq(Share::getShareBatchNum, shareBatchNum));
        if (share == null) {
            return CommonResult.failed("文件不存在");
        }
        return new Date().after(share.getEndTime())
                ? CommonResult.failed("分享已过期")
                : CommonResult.success(null);
    }
}
