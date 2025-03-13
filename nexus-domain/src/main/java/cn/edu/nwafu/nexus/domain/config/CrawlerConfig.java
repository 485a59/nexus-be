package cn.edu.nwafu.nexus.domain.config;

import cn.edu.nwafu.nexus.domain.crawler.ArticleCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CrawlerConfig {

    @Bean
    public Map<String, ArticleCrawler> crawlerMap(@Autowired List<ArticleCrawler> crawlers) {
        Map<String, ArticleCrawler> crawlerMap = new HashMap<>();
        for (ArticleCrawler crawler : crawlers) {
            // 从类名获取 bean 名称，例如 SpringerOpenCrawler -> springerOpenCrawler
            String name = crawler.getClass().getSimpleName();
            // 首字母小写
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            crawlerMap.put(name, crawler);
        }
        return crawlerMap;
    }
} 