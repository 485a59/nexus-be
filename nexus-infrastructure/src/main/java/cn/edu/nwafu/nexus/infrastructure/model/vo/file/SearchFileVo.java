package cn.edu.nwafu.nexus.infrastructure.model.vo.file;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Huang Z.Y.
 */
@Data
public class SearchFileVo {
    private String id;
    private String name;
    private String path;
    private String extension;
    private Long size;
    private String url;
    private Map<String, List<String>> highLight;
    private Integer isDir;
}
