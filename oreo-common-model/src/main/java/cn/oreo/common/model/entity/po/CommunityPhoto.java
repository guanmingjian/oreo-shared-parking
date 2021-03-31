package cn.oreo.common.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author GuanMingJian
 * @since 2021-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_community_photo")
@ApiModel(value="CommunityPhoto对象", description="")
public class CommunityPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "图片路径")
    @TableField("photo_url")
    private String photoUrl;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "新增时间")
    @TableField("add_time")
    private Date addTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "编辑时间")
    @TableField("update_time")
    private Date updateTime;

    @TableField("deleted")
    private Integer deleted;
}
