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
 * @author Huang Z.Y.
 */
@Data
@TableName("file_permission")
@ApiModel(description = "文件权限信息表")
public class FilePermission extends BaseEntity<FilePermission> {
    @ApiModelProperty("文件权限 ID")
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;

    @ApiModelProperty("共享文件 ID")
    @TableField("file_id")
    public String fileId;

    @ApiModelProperty("用户 ID")
    @TableField("user_id")
    public Long userId;

    @ApiModelProperty("用户对文件的权限码")
    @TableField("code")
    public Integer code;
}

