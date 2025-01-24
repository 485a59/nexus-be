package cn.edu.nwafu.nexus.infrastructure.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件扩展名信息表。
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("file_extension")
@ApiModel(description = "文件扩展名信息表")
public class FileExtension extends BaseEntity<FileExtension> {
    @ApiModelProperty("文件拓展名 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("文件扩展名")
    @TableField("name")
    private String name;

    @ApiModelProperty("文件扩展名描述")
    @TableField("desc")
    private String desc;

    @ApiModelProperty("文件扩展名预览图 URL")
    @TableField("img_url")
    private String imgUrl;
}

