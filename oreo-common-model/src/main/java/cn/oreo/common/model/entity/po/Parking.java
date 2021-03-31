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
 * @author XuMin
 * @since 2021-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_parking")
@ApiModel(value="Parking对象", description="")
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "停车位所属的用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "停车位所属的用户名称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "停车位所属的小区id")
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "停车位所属的小区名字")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "停车位编号")
    @TableField("parking_sn")
    private String parkingSn;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "添加时间")
    @TableField("add_time")
    private Date addTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "车位状态（0：可用 1：禁用）")
    @TableField("parking_status")
    private String parkingStatus;

    @ApiModelProperty(value = "停车位逻辑状态：0可用，1逻辑删除")
    @TableField("deleted")
    private String deleted;

}
