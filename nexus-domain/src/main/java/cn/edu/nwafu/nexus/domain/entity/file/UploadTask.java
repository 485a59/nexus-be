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
 * 上传任务信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("upload_task")
@ApiModel(description = "上传任务信息表")
public class UploadTask extends BaseEntity<UploadTask> {
    @ApiModelProperty("上传任务 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户 ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("MD5 唯一标识")
    @TableField("identifier")
    private String identifier;

    @ApiModelProperty("文件名称")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("文件路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty("扩展名")
    @TableField("extension")
    private String extension;

    @ApiModelProperty("上传时间")
    @TableField("upload_time")
    private String uploadTime;

    @ApiModelProperty("上传状态 (1 表示成功，0 表示失败或未完成)")
    @TableField("upload_status")
    private Integer uploadStatus;
}
