package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import cn.edu.nwafu.nexus.ufop.operation.upload.domain.UploadFileResult;
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
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    public File(UploadFileResult uploadFileResult) {
        this.url = uploadFileResult.getFileUrl();
        this.size = uploadFileResult.getFileSize();
        this.status = 1;
        this.storageType = uploadFileResult.getStorageType().getCode();
        this.identifier = uploadFileResult.getIdentifier();
    }

    public File(String fileUrl, Long fileSize, Integer storageType, String identifier, String userId) {
        this.url = fileUrl;
        this.size = fileSize;
        this.status = 1;
        this.storageType = storageType;
        this.identifier = identifier;
        this.createUserId = userId;
    }
}
