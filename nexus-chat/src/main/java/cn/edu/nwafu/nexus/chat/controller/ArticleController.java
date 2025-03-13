package cn.edu.nwafu.nexus.chat.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.ArticleService;
import cn.edu.nwafu.nexus.infrastructure.model.vo.article.ArticleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @ApiOperation("获取所有文章列表")
    @GetMapping
    public CommonResult<List<ArticleVo>> getArticles() {
        try {
            List<ArticleVo> articles = articleService.fetchArticles();
            if (articles.isEmpty()) {
                return CommonResult.failed("未找到文章或爬取失败");
            }
            return CommonResult.success(articles);
        } catch (Exception e) {
            log.error("Failed to get articles", e);
            return CommonResult.failed("获取文章失败: " + e.getMessage());
        }
    }
} 