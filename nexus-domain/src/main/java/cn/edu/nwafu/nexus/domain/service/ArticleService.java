package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.vo.article.ArticleVo;
import java.util.List;

public interface ArticleService {
    /**
     * 获取所有文章列表
     */
    List<ArticleVo> fetchArticles();
    
    /**
     * 同步文章到数据库
     */
    void syncArticles();
}