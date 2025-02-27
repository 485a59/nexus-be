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

/**
 * 文件类型。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("file_type")
@ApiModel(description = "文件类型表")
public class FileType extends BaseEntity<FileType> {
    @ApiModelProperty("文件类型编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件类型名")
    @TableField("name")
    private String name;

    @ApiModelProperty("次序")
    @TableField("order")
    private String order;
}
