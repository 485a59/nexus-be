package cn.edu.nwafu.nexus.domain.entity.file;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用文件信息表。
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("common_file")
@ApiModel(description = "通用文件信息表")
public class CommonFile extends BaseEntity<CommonFile> {
    @ApiModelProperty("通用文件 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("用户文件 ID")
    @TableField("user_file_id")
    private String userFileId;

//    @ApiModelProperty("文件权限")
//    @TableField("filePermission")
//    public Integer filePermission;
}