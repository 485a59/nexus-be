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
 * 上传任务详细信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("upload_task_detail")
@ApiModel(description = "上传任务详细信息表")
public class UploadTaskDetail extends BaseEntity<UploadTaskDetail> {
    @ApiModelProperty("上传任务详细信息 ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("文件路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty("文件名称")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("当前分片数")
    @TableField("chunk_number")
    private int chunkNumber;

    @ApiModelProperty("当前分片大小")
    @TableField("chunk_size")
    private Integer chunkSize;

    @ApiModelProperty("文件相对路径")
    @TableField("relative_path")
    private String relativePath;

    @ApiModelProperty("文件总分片数")
    @TableField("total_chunks")
    private Integer totalChunks;

    @ApiModelProperty("文件总大小")
    @TableField("total_size")
    private Integer totalSize;

    @ApiModelProperty("文件 MD5 唯一标识")
    @TableField("identifier")
    private String identifier;
}
