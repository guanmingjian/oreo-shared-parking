package cn.oreo.common.model.entity.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author GuanMingJian
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
@ApiModel(value="Order对象", description="")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
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

    @ApiModelProperty(value = "流水号(code)")
    @TableField("order_sn")
    private String orderSn;

    @ApiModelProperty(value = "订单状态(1、进行时   2、完成  3、取消)")
    @TableField("order_status")
    private String orderStatus;

    @ApiModelProperty(value = "支付状态（1、未支付   2、已支付    3、已退款）")
    @TableField("pay_status")
    private String payStatus;

    @ApiModelProperty(value = "支付人名字")
    @TableField("pay_name")
    private String payName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "支付时间")
    @TableField("pay_time")
    private Date payTime;

    @ApiModelProperty(value = "订单总价")
    @TableField("order_price")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "实际需要支付的金额")
    @TableField("actual_price")
    private BigDecimal actualPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "下单时间")
    @TableField("add_time")
    private Date addTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "订单完成时间")
    @TableField("complete_time")
    private Date completeTime;

    @ApiModelProperty(value = "预约入场时间")
    @TableField("enter_time")
    private Integer enterTime;

    @ApiModelProperty(value = "预约出场时间")
    @TableField("leave_time")
    private Integer leaveTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "修改订单的记录时间")
    @TableField("update_time")
    private Date updateTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "取消订单的记录时间")
    @TableField("cancel_time")
    private Date cancelTime;

    @ApiModelProperty(value = "冗余车辆车牌号")
    @TableField("car_number")
    private String carNumber;

    @ApiModelProperty(value = "冗余小区名字")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "冗余停车位编号")
    @TableField("parking_sn")
    private String parkingSn;

    @ApiModelProperty(value = "冗余小区地址")
    @TableField("community_address")
    private String communityAddress;

    /**
     * 冗余小区图片，用于返回给前端
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "冗余小区图片")
    private List<CommunityPhoto> communityPhotoList;

    @Version
    @TableField("version")
    @ApiModelProperty(value = "乐观锁（防止更新时被别人更新）")
    private Integer version;

    @TableField("deleted")
    private String deleted;

    @TableField("out_trade_no")
    @ApiModelProperty(value = "商品单号")
    private  String outTradeNo;

    @TableField("rates")
    @ApiModelProperty(value = "收费方式")
    private Integer rates;

}
