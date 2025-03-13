package cn.edu.nwafu.nexus.domain.crawler.impl;

import cn.edu.nwafu.nexus.domain.crawler.ArticleCrawler;
import cn.edu.nwafu.nexus.infrastructure.model.vo.article.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component("kr36AICrawler")
public class Kr36AICrawler implements ArticleCrawler {
    public static final String KR36AI_SOURCE = "Kr36AI";
    private static final String BASE_URL = "https://36kr.com/information/AI/";

    @Override
    public List<ArticleVo> crawlArticles() {
        List<ArticleVo> articles = new ArrayList<>();
        try {
            Document document = Jsoup.connect(BASE_URL).get();
            Elements articleElements = document.select(".information-flow-item");

            for (Element article : articleElements) {
                ArticleVo articleVo = new ArticleVo();
                // 提取标题
                articleVo.setTitle(article.select(".article-item-title").text());
                // 设置当前日期
                articleVo.setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                // 提取作者
                articleVo.setAuthors(List.of(article.select(".kr-flow-bar-author").text()));
                // 提取链接
                articleVo.setUrl("https://36kr.com" + article.select(".article-item-title").attr("href"));
                // 提取描述
                articleVo.setDescription(article.select(".article-item-description").text());
                articleVo.setSource(KR36AI_SOURCE);
                articles.add(articleVo);
            }
        } catch (Exception e) {
            log.error("Failed to crawl 36Kr AI articles", e);
        }
        return articles;
    }
} 