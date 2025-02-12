package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.ChapterService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.ChapterListDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.ChapterListVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@RequestMapping("/system/chapter")
@Api(tags = "课程章节 API")
@Tag(name = "课程章节 API", description = "此接口主要用于对课程章节进行管理操作，涵盖部门信息的创建、查询、更新以及删除等功能")
@RestController
@Slf4j
public class SysChapterController {
    @Resource
    private ChapterService chapterService;

    @Operation(summary = "部门列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<List<ChapterListVo>> list(@RequestBody ChapterListDto query) {
        List<ChapterListVo> list = chapterService.list(query);
        return CommonResult.success(list);
    }
}
