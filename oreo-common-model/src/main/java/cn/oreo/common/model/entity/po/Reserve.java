package cn.oreo.common.model.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author GuanMingJian
 * @since 2021-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_reserve")
@ApiModel(value="Reserve对象", description="预约表")
public class Reserve implements Serializable {

    private static final long serialVersionUID = 1L;

    // type= IdType.INPUT 自定义输入主键id
    @TableId(value = "id",type= IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "小区id")
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "车辆id")
    @TableField("car_id")
    private Long carId;

    @ApiModelProperty(value = "停车位id")
    @TableField("parking_id")
    private Long parkingId;

    @ApiModelProperty(value = "预约序列号")
    @TableField("reserve_sn")
    private String reserveSn;

    @ApiModelProperty(value = "预约状态（1、预约成功 2、预约过期 3、手动取消  4、下单成功 5、预约完成）")
    @TableField("reserve_status")
    private String reserveStatus;

    @ApiModelProperty(value = "支付状态（1、未支付  2、已支付  3、已退款）")
    @TableField("pay_status")
    private String payStatus;

    @ApiModelProperty(value = "支付方式（1：微信支付 2：支付宝支付）")
    @TableField("pay_type")
    private String payType;

    @ApiModelProperty(value = "支付id")
    @TableField("pay_id")
    private Long payId;

    @ApiModelProperty(value = "支付人名字")
    @TableField("pay_name")
    private String payName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "支付时间")
    @TableField("pay_time")
    private Date payTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "预约日期（yyyy-MM-dd）")
    @TableField("reserve_date")
    private Date reserveDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "预约时间")
    @TableField("reserve_time")
    private Date reserveTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "取消预约时间")
    @TableField("reserve_cancel_time")
    private Date reserveCancelTime;

    @ApiModelProperty(value = "担保金类型（1：押金 2：信用）")
    @TableField("earnest_type")
    private String earnestType;

    @ApiModelProperty(value = "担保金金额")
    @TableField("earnest_price")
    private BigDecimal earnestPrice;

    @ApiModelProperty(value = "担保金退还金额")
    @TableField("earnest_refund_price")
    private BigDecimal earnestRefundPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "担保金金退还时间")
    @TableField("earnest_refund_time")
    private Date earnestRefundTime;

    @ApiModelProperty(value = "预约入场时间")
    @TableField("enter_time")
    private Integer enterTime;

    @ApiModelProperty(value = "预约出场时间")
    @TableField("leave_time")
    private Integer leaveTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "修改预约时间的记录时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "冗余车辆车牌号")
    @TableField("car_number")
    private String carNumber;

    @ApiModelProperty(value = "冗余小区名字")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "冗余小区地址")
    @TableField("community_address")
    private String communityAddress;

    @ApiModelProperty(value = "冗余停车位编号")
    @TableField("parking_sn")
    private String parkingSn;

    @ApiModelProperty(value = "乐观锁（防止更新时被别人更新）")
    @TableField("version")
    private Integer version;

    @TableField("deleted")
    private String deleted;

    @ApiModelProperty(value = "商品单号")
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * 冗余小区图片，用于返回给前端
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "冗余小区图片")
    private List<CommunityPhoto> communityPhotoList;

    @TableField("rates")
    @ApiModelProperty(value = "收费方式")
    private Integer rates;
}
