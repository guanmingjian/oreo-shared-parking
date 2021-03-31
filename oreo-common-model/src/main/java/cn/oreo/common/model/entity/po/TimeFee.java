package cn.oreo.common.model.entity.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author GuanMingJian
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_time_fee")
@ApiModel(value="TimeFee对象", description="")
public class TimeFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "包时设置的时间（半小时为单位）")
    @TableField("time")
    private Integer time;

    @ApiModelProperty(value = "套餐费用")
    @TableField("time_fee")
    private BigDecimal timeFee;

    @ApiModelProperty(value = "超过套餐时间所需要的单价（半小时为单位）")
    @TableField("extra_unit_fee")
    private BigDecimal extraUnitFee;

    @ApiModelProperty(value = "描述")
    @TableField("time_description")
    private String timeDescription;

    @TableField("add_time")
    private Date addTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("deleted")
    private Integer deleted;

}
