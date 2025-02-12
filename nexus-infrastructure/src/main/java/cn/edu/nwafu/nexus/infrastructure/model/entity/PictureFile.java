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
 * 图片文件信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("picture_file")
@ApiModel(description = "图片文件信息表")
public class PictureFile extends BaseEntity<PictureFile> {
    @ApiModelProperty("图片文件编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long pictureFileId;

    @ApiModelProperty("图片文件 URL")
    @TableField("url")
    private String url;

    @ApiModelProperty("文件大小")
    @TableField("size")
    private Long fileSize;

    @ApiModelProperty("存储类型")
    @TableField("storage_type")
    private Integer storageType;

    @ApiModelProperty("用户 ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("文件名称")
    @TableField("fileName")
    private String fileName;

    @ApiModelProperty("扩展名")
    @TableField("extension")
    private String extension;
}