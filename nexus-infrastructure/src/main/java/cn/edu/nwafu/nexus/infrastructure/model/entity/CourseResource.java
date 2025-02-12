package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 课程资源信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("course_resource")
@ApiModel(description = "课程资源信息表")
public class CourseResource extends BaseEntity<CourseResource> {
    @ApiModelProperty("资源ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    @ApiModelProperty("资源名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("章节ID")
    @TableField("chapter_id")
    private Integer chapterId;

    @ApiModelProperty("用户文件ID")
    @TableField("file_id")
    private String fileId;

    @ApiModelProperty("资源类型(1-教材,2-视频,3-软件,4-课件)")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("元数据(JSON格式)")
    @TableField("meta")
    private String meta;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;
} 