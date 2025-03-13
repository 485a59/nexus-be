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
import java.util.Locale;

@Slf4j
@Component("springerOpenCrawler")
public class SpringerOpenCrawler implements ArticleCrawler {

    public static final String SPRING_OPEN_SOURCE = "springeropen";
    private static final String BASE_URL = "https://cybersecurity.springeropen.com/";

    @Override
    public List<ArticleVo> crawlArticles() {
        List<ArticleVo> articles = new ArrayList<>();
        try {
            Document document = Jsoup.connect(BASE_URL).get();
            Elements articleElements = document.select("article");

            for (Element article : articleElements) {
                ArticleVo articleVo = new ArticleVo();

                // 提取标题
                articleVo.setTitle(article.select("h3.c-listing__title").text());

                // 提取日期
                String rawDate = article.select("div.c-listing__metadata").text();
                articleVo.setDate(formatDate(rawDate));

                // 提取链接
                articleVo.setUrl(BASE_URL + article.select("h3.c-listing__title>a").attr("href"));

                // 提取作者
                String authorText = article.select("p.c-listing__authors").text();
                articleVo.setAuthors(parseAuthors(authorText));

                articleVo.setSource(SPRING_OPEN_SOURCE);

                articles.add(articleVo);
            }
        } catch (Exception e) {
            log.error("Failed to crawl Springer Open articles", e);
        }
        return articles;
    }

    private String formatDate(String rawDate) {
        try {
            String[] parts = rawDate.split(" ");
            String dateString = parts[parts.length - 3] + " " + parts[parts.length - 2] + " " + parts[parts.length - 1];

            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            log.error("Failed to parse date: {}", rawDate, e);
            return "Invalid Date";
        }
    }

    /**
     * 解析作者列表
     * 从 "Authors: Author1, Author2 and Author3" 格式解析出作者列表
     */
    private List<String> parseAuthors(String authorText) {
        List<String> authors = new ArrayList<>();
        try {
            if (authorText.startsWith("Authors: ")) {
                // 移除 "Authors: " 前缀
                String names = authorText.substring("Authors: ".length());
                // 先按 " and " 分割
                String[] parts = names.split(" and ");
                if (parts.length > 1) {
                    // 处理最后一个作者
                    authors.add(parts[parts.length - 1].trim());
                    // 处理前面的作者（可能包含逗号分隔的多个作者）
                    String[] otherAuthors = parts[0].split(",");
                    for (String author : otherAuthors) {
                        if (!author.trim().isEmpty()) {
                            authors.add(author.trim());
                        }
                    }
                } else {
                    // 如果没有 " and "，就只按逗号分割
                    String[] simpleAuthors = names.split(",");
                    for (String author : simpleAuthors) {
                        if (!author.trim().isEmpty()) {
                            authors.add(author.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse authors from: {}", authorText, e);
        }
        return authors;
    }
} 