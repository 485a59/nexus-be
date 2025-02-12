package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 章节信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("chapter")
@ApiModel(description = "章节信息表")
public class Chapter extends BaseEntity<Chapter> {
    @ApiModelProperty("章节ID")
    @TableId("id")
    private Integer id;

    @ApiModelProperty("章节名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("父章节号")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty("排序序号")
    @TableField("order_num")
    private Integer orderNum;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("状态(0 停用,1 启用)")
    @TableField("status")
    private Integer status;
} 