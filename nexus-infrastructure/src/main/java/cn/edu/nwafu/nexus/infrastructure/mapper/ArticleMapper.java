package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据URL检查文章是否存在
     */
    boolean existsByUrl(@Param("url") String url);

    /**
     * 获取文章列表（包含作者信息）
     */
    List<Article> selectArticleList();

    /**
     * 根据ID获取文章（包含作者信息）
     */
    Article selectArticleById(@Param("id") Long id);
} 