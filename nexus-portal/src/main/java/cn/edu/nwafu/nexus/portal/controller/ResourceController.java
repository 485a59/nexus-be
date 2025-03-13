package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.CourseResourceService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.TextbookListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.curriculum.VideoListDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum.*;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Huang Z.Y.
 */
@RestController
@Slf4j
@Api(tags = "课程资源 API")
@Tag(name = "课程资源 API", description = "该接口为课程资源接口")
@RequestMapping("resource")
public class ResourceController {
    @Resource
    private CourseResourceService courseResourceService;

    public static <T> List<T> batchQuery(BiFunction<Integer, Integer, List<T>> queryFunction, int batchSize) {
        List<T> result = new ArrayList<>();
        int pageNum = 1;

        while (true) {
            // 查询当前批次的数据
            List<T> batchList = queryFunction.apply(batchSize, pageNum);
            if (batchList.isEmpty()) {
                break; // 如果没有数据了，退出循环
            }

            // 将当前批次的数据添加到结果中
            result.addAll(batchList);
            pageNum++; // 查询下一页
        }

        return result;
    }

    @Operation(summary = "教材文件")
    @PostMapping("/textbook/list")
    public CommonResult<List<TextbookListVo>> listTextbook(@RequestBody TextbookListDto command) {
        // 定义查询函数
        BiFunction<Integer, Integer, List<TextbookListVo>> queryFunction = (pageSize, pageNum) ->
                courseResourceService.listTextbook(command, pageSize, pageNum);
        // 调用泛型函数进行分批查询
        List<TextbookListVo> result = batchQuery(queryFunction, 1000);
        return CommonResult.success(result);
    }

    @Operation(summary = "视频文件")
    @PostMapping("/video/list")
    public CommonResult<List<VideoListVo>> listVideo(@RequestBody VideoListDto command) {
        BiFunction<Integer, Integer, List<VideoListVo>> queryFunction = (pageSize, pageNum) ->
                courseResourceService.listVideo(command, pageSize, pageNum);
        List<VideoListVo> result = batchQuery(queryFunction, 1000);
        return CommonResult.success(result);
    }

    @Operation(summary = "视频资源树")
    @GetMapping("/video/tree")
    public CommonResult<List<VideoTreeNodeVo>> treeVideo() {
        List<VideoTreeNodeVo> list = courseResourceService.listVideoTree();
        return CommonResult.success(list);
    }

    @Operation(summary = "课件资源树")
    @GetMapping("/slide/tree")
    public CommonResult<List<SlideTreeNodeVo>> treeSlide() {
        List<SlideTreeNodeVo> list = courseResourceService.listSlideTree();
        return CommonResult.success(list);
    }

    @Operation(summary = "软件分类树")
    @GetMapping("/software/tree")
    public CommonResult<List<SoftwareTreeNodeVo>> treeSlideTree() {
        List<SoftwareTreeNodeVo> list = courseResourceService.listSoftwareTree();
        return CommonResult.success(list);
    }

}
