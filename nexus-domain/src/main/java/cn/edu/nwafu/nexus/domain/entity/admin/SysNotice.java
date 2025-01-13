package cn.edu.nwafu.nexus.domain.entity.admin;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 公告表。
 *
 * @author Huang Z.Y.
 */
@TableName("sys_notice")
@ApiModel(description = "通知公告表")
@Getter
@Setter
public class SysNotice extends BaseEntity<SysNotice> {
    @Serial
    private static final long serialVersionUID = 7442653721343L;

    @ApiModelProperty("公告ID")
    @TableId(value = "id", type = IdType.AUTO)
    @TableField("id")
    private Integer id;

    @ApiModelProperty("公告标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("公告类型（1通知 2公告）")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("公告内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("公告状态（1正常 0关闭）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
