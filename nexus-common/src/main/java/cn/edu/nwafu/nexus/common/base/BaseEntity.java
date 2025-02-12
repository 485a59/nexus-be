package cn.edu.nwafu.nexus.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * 实体基类。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity<T extends Model<?>> extends Model<T> {
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)

    protected Date updateTime;

    @ApiModelProperty("删除时间")
    @Nullable
    @TableField(value = "delete_time", exist = false)
    protected Date deleteTime;
}
