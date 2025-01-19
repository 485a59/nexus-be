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
 * 分享信息表。
 *
 * @author Huang Z.Y.
 */
@Setter
@Getter
@TableName("share")
@ApiModel(description = "分享信息表")
public class Share extends BaseEntity<Share> {
    @ApiModelProperty("分享编号")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("用户 ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty("分享时间")
    @TableField("share_time")
    private String shareTime;

    @ApiModelProperty("失效时间")
    @TableField("end_time")
    private String endTime;

    @ApiModelProperty("提取码")
    @TableField("extraction_code")
    private String extractionCode;

    @ApiModelProperty("分享批次号")
    @TableField("share_batch_num")
    private String shareBatchNum;

    @ApiModelProperty("分享类型 (0 公共, 1 私密, 2 好友)")
    @TableField("share_type")
    private Integer shareType;

    @ApiModelProperty("分享状态 (0 正常, 1 已失效, 2 已撤销)")
    @TableField("share_status")
    private Integer shareStatus;
}
