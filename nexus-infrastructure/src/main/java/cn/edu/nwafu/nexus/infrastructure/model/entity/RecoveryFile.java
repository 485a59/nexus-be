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
 * @author Huang Z.Y.
 */
@Setter
@Getter
@TableName("recovery_file")
@ApiModel(description = "回收站文件")
public class RecoveryFile extends BaseEntity<RecoveryFile> {
    @ApiModelProperty("回收站文件编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户文件 ID")
    @TableField("user_file_id")
    private String userFileId;

    @ApiModelProperty("删除批次号")
    @TableField("delete_batch_num")
    private String deleteBatchNum;
}
