package cn.oreo.common.model.entity.dto.server.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UnitFeeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区id")
    @NotNull
    private Long communityId;

    @ApiModelProperty(value = "费用（半小时为单位）")
    @NotNull
    private BigDecimal unitFee;


    @ApiModelProperty(value = "描述,非必填")
    private String unitDescription;

}
