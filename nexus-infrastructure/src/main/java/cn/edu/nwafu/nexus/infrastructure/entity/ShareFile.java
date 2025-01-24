package cn.edu.nwafu.nexus.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分享文件信息表。
 *
 * @author Huang Z.Y.
 */
@TableName("share_file")
@ApiModel(description = "分享文件信息表")
public class ShareFile {
    @ApiModelProperty("分享文件编号")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("分享批次号")
    @TableField("share_batch_num")
    private String shareBatchNum;

    @ApiModelProperty("用户文件 ID")
    @TableField("user_file_id")
    private String userFileId;

    @ApiModelProperty("分享文件路径")
    @TableField("share_file_path")
    private String shareFilePath;
}
