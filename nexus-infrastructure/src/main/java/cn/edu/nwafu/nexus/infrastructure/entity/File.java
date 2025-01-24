package cn.edu.nwafu.nexus.infrastructure.entity;

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
 * 文件信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("file")
@ApiModel(description = "文件信息表")
public class File extends BaseEntity<File> {
    @ApiModelProperty("文件 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("文件 URL")
    @TableField("url")
    private String url;

    @ApiModelProperty("文件大小")
    @TableField("size")
    private Long size;

    @ApiModelProperty("文件状态 (0-失效，1-生效)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("存储类型")
    @TableField("storage_type")
    private Integer storageType;

    @ApiModelProperty("MD5 唯一标识")
    @TableField("identifier")
    private String identifier;

    @ApiModelProperty("创建用户 ID")
    @TableField("create_user_id")
    private String createUserId;

    @ApiModelProperty("修改用户 ID")
    @TableField("modify_user_id")
    private String modifyUserId;

    public File() {
    }
}
