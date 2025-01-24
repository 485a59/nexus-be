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

import java.util.Date;

/**
 * 用户文件信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("user_file")
@ApiModel(description = "用户文件信息表")
public class UserFile extends BaseEntity<UserFile> {
    @ApiModelProperty("用户文件 ID")
    @TableId(value = "userFileId", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("用户 ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty("文件 ID")
    @TableField("file_id")
    private String fileId;

    @ApiModelProperty("文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("文件路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty("扩展名")
    @TableField("extension")
    private String extension;

    @ApiModelProperty("是否是目录 (0-否, 1-是)")
    @TableField("is_dir")
    private Integer isDir;

    @ApiModelProperty("修改时间")
    @TableField("upload_time")
    private Date uploadTime;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private Date deleteTime;

    @ApiModelProperty("删除批次号")
    @TableField("delete_batch_num")
    private String deleteBatchNum;

    @ApiModelProperty("创建用户 ID")
    @TableField("create_user_id")
    private String createUserId;

    @ApiModelProperty("修改用户 ID")
    @TableField("modify_user_id")
    private String modifyUserId;

    public UserFile() {
    }

    public boolean isDirectory() {
        return this.isDir == 1;
    }

    public boolean isFile() {
        return this.isDir == 0;
    }
}
