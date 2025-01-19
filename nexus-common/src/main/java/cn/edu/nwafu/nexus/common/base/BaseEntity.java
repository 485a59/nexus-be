package cn.edu.nwafu.nexus.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实体基类
 *
 * @author Huang Z.Y.
 */
public class BaseEntity<T extends Model<?>> extends Model<T> {
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    protected Date updateTime;

    /**
     * deleted 字段请在数据库中设置为 tinyInt，并且非 null，默认值为 0
     */
    @ApiModelProperty("删除标志（0代表存在 1代表删除）")
    @TableField("deleted")
    @TableLogic
    protected Boolean deleted;
}
