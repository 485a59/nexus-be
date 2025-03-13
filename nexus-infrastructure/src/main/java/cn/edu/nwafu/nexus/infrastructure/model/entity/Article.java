package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("article")
@ApiModel(description = "文章表")
public class Article extends BaseEntity<Article> {

    @ApiModelProperty("文章ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文章标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("发布日期")
    @TableField("date")
    private String date;

    @ApiModelProperty("文章链接")
    @TableField("url")
    private String url;

    @ApiModelProperty("文章描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("文章来源")
    @TableField("source")
    private String source;

    @ApiModelProperty("作者列表")
    @TableField("authors")
    private String authors;  // 存储格式：["author1","author2","author3"]
} 