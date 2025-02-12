package cn.edu.nwafu.nexus.infrastructure.model.meta;

import lombok.Data;

import java.util.Date;

/**
 * 教材元数据。
 */
@Data
public class TextbookMeta {
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 发行日期
     */
    private Date publishDate;
    /**
     * ISBN号
     */
    private String isbn;
    /**
     * 作者
     */
    private String author;
    /**
     * 版次
     */
    private String edition;
} 