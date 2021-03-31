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
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_verify")
@ApiModel(value="UserVerify对象", description="")
public class UserVerify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "用户小区表id")
    @TableField("user_community_id")
    private Long userCommunityId;

    @ApiModelProperty(value = "用户id")
    @TableField("parking_id")
    private Long parkingId;

    /**
     * 冗余用户信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "冗余用户信息")
    private User user;

    @ApiModelProperty(value = "小区id")
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "冗余小区名称")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "停车位编号")
    @TableField("parking_sn")
    private String parkingSn;

    @ApiModelProperty(value = "产权人名字")
    @TableField("owner_name")
    private String ownerName;

    @ApiModelProperty(value = "身份证号码")
    @TableField("id_card")
    private String idCard;

    @ApiModelProperty(value = "证明材料图片路径")
    @TableField("probative_url")
    private String probativeUrl;

    @ApiModelProperty(value = "审核状态（1、审核中 2、审核通过 3、审核不通过）")
    @TableField("verify_status")
    private String verifyStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "申请审核时间")
    @TableField("apply_time")
    private Date applyTime;

    @ApiModelProperty(value = "处理审核时间")
    @TableField("verify_time")
    private Date verifyTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;

}
