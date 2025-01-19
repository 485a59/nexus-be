package cn.edu.nwafu.nexus.domain.entity.file;

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
 * 存储信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("storage")
@ApiModel(description = "存储信息表")
public class Storage extends BaseEntity<Storage> {
    @ApiModelProperty("存储编号")
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;

    @ApiModelProperty("用户 ID")
    @TableField("user_id")
    public String userId;

    @ApiModelProperty("占用存储大小")
    @TableField("size")
    public Long storageSize;

    @ApiModelProperty("总存储大小")
    @TableField("total_size")
    private Long totalStorageSize;
}
