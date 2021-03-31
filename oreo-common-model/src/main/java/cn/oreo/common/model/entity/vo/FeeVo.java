package cn.oreo.common.model.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class FeeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "t_time_fee表主键")
    private Long timeFeeId;

    @ApiModelProperty(value = "t_unit_fee表主键")
    private Long unitFeeId;

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

    @ApiModelProperty(value = "包时套餐描述")
    @TableField("time_description")
    private String timeDescription;

    //---------------------------

    @ApiModelProperty(value = "费用（半小时为单位）")
    @TableField("unit_fee")
    private BigDecimal unitFee;

    @ApiModelProperty(value = "描述")
    @TableField("unit_description")
    private String unitDescription;

    //----------------------------

    @ApiModelProperty(value = "担保金金额")
    @TableField("earnest_price")
    private BigDecimal earnestPrice;

}
