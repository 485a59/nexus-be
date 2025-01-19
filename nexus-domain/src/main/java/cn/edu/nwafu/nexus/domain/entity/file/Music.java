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
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("music")
@ApiModel(description = "音乐信息表")
public class Music extends BaseEntity<Music> {
    @ApiModelProperty("音乐编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文件 ID")
    @TableField("file_id")
    private Long fileId;

    @ApiModelProperty("音轨")
    @TableField("track")
    private String track;

    @ApiModelProperty("艺术家")
    @TableField("artist")
    private String artist;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("专辑")
    @TableField("album")
    private String album;

    @ApiModelProperty("年份")
    @TableField("year")
    private String year;

    @ApiModelProperty("流派")
    @TableField("genre")
    private String genre;

    @ApiModelProperty("评论")
    @TableField("comment")
    private String comment;

    @ApiModelProperty("歌词")
    @TableField("lyrics")
    private String lyrics;

    @ApiModelProperty("作曲家")
    @TableField("composer")
    private String composer;

    @ApiModelProperty("发布者")
    @TableField("publisher")
    private String publisher;

    @ApiModelProperty("原唱艺术家")
    @TableField("original_artist")
    private String originalArtist;

    @ApiModelProperty("专辑艺术家")
    @TableField("album_artist")
    private String albumArtist;

    @ApiModelProperty("版权")
    @TableField("copyright")
    private String copyright;

    @ApiModelProperty("URL")
    @TableField("url")
    private String url;

    @ApiModelProperty("编码器")
    @TableField("encoder")
    private String encoder;

    @ApiModelProperty("专辑图片")
    @TableField("album_image")
    private String albumImage;

    @ApiModelProperty("音轨长度")
    @TableField("track_length")
    private Float trackLength;
}
