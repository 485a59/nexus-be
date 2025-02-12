package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
@TableName("file_category")
@ApiModel(description = "文件分类信息表")
public class FileCategory extends BaseEntity<FileCategory> {
    @ApiModelProperty("文件分类 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文件类型 ID")
    @TableField("type_id")
    private Integer fileTypeId;

    @ApiModelProperty("文件扩展名")
    @TableField("extension")
    private String extension;
}
