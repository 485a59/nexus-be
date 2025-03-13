package cn.edu.nwafu.nexus.domain.crawler;

import cn.edu.nwafu.nexus.infrastructure.model.vo.article.ArticleVo;
import java.util.List;

public interface ArticleCrawler {
    /**
     * 爬取文章列表
     *
     * @return 文章列表
     */
    List<ArticleVo> crawlArticles();
} 