package cn.edu.nwafu.nexus.infrastructure.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统参数信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("sys_param")
@ApiModel(description = "系统参数表")
public class SysParam {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("参数编号")
    private Long id;

    @TableField(value = "group_name")
    @ApiModelProperty("组名")
    private String groupName;

    @TableField(value = "key")
    @ApiModelProperty("键名")
    private String key;

    @TableField(value = "value")
    @ApiModelProperty("值")
    private String value;

    @TableField(value = "description")
    @ApiModelProperty("描述")
    private String description;
}
