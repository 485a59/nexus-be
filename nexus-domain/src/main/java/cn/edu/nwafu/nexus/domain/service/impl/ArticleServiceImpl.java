package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.crawler.ArticleCrawler;
import cn.edu.nwafu.nexus.domain.service.ArticleService;
import cn.edu.nwafu.nexus.infrastructure.mapper.ArticleMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Article;
import cn.edu.nwafu.nexus.infrastructure.model.vo.article.ArticleVo;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private Map<String, ArticleCrawler> crawlers;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleVo> fetchArticles() {
        return articleMapper.selectArticleList().stream()
                .map(this::convertToVo)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000) // 每7天执行一次
    @Override
    public void syncArticles() {
        log.info("Starting article sync task");
        List<ArticleVo> newArticles = new ArrayList<>();

        // 从所有爬虫获取文章
        for (ArticleCrawler crawler : crawlers.values()) {
            try {
                List<ArticleVo> articles = crawler.crawlArticles();
                newArticles.addAll(articles);
            } catch (Exception e) {
                log.error("Failed to fetch articles from crawler: {}",
                        crawler.getClass().getSimpleName(), e);
            }
        }

        // 保存新文章到数据库
        for (ArticleVo articleVo : newArticles) {
            try {
                if (!articleMapper.existsByUrl(articleVo.getUrl())) {
                    Article article = convertToEntity(articleVo);
                    articleMapper.insert(article);
                    log.info("Saved new article: {}", article.getTitle());
                }
            } catch (Exception e) {
                log.error("Failed to save article: {}", articleVo.getTitle(), e);
            }
        }

        log.info("Article sync task completed");
    }

    private ArticleVo convertToVo(Article article) {
        ArticleVo vo = new ArticleVo();
        vo.setTitle(article.getTitle());
        vo.setDate(article.getDate());
        vo.setAuthors(JSON.parseArray(article.getAuthors(), String.class));
        vo.setUrl(article.getUrl());
        vo.setDescription(article.getDescription());
        vo.setSource(article.getSource());
        return vo;
    }

    private Article convertToEntity(ArticleVo vo) {
        Article article = new Article();
        article.setTitle(vo.getTitle());
        article.setDate(vo.getDate());
        article.setAuthors(JSON.toJSONString(vo.getAuthors()));
        article.setUrl(vo.getUrl());
        article.setDescription(vo.getDescription());
        article.setSource(vo.getSource());
        return article;
    }
} 