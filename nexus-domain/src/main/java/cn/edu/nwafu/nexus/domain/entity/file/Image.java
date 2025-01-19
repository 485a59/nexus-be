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
 * 图像表。
 *
 * @author Huang Z.Y.
 */
@Setter
@Getter
@TableName("image")
@ApiModel(description = "图像表")
public class Image extends BaseEntity<Image> {
    @ApiModelProperty("图像编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文件编号")
    @TableField("file_id")
    private String fileId;

    @ApiModelProperty("图片的宽度")
    @TableField("width")
    private Integer width;

    @ApiModelProperty("图片的高度")
    @TableField("图像的高")
    private Integer height;
}