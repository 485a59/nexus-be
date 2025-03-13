package cn.edu.nwafu.nexus.infrastructure.model.vo.article;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    private String title;
    private String date;
    private List<String> authors;
    private String url;
    private String description;
    private String source;
} 