package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class TextbookListVo {
    private String id;
    private String name;
    private String publisher;
    private String isbn;
    private String author;
    private String edition;
    private String fileId;
    private String url;
}

