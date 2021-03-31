package cn.oreo.common.model.entity.dto.server.admin;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TimeFeeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区id")
    @NotNull
    private Long communityId;

    @ApiModelProperty(value = "套餐时间")
    @NotNull
    private Integer time;

    @ApiModelProperty(value = "套餐费用")
    @NotNull
    private BigDecimal timeFee;

    @ApiModelProperty(value = "超过套餐时间所需要的单价（半小时为单位）")
    private BigDecimal extraUnitFee;

    @ApiModelProperty(value = "描述，非必选")
    private String timeDescription;
}
