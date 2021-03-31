package cn.oreo.common.model.entity.dto.server.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuanMingJian
 * @since 2021/1/20
 */
@Data
public class AssignParkingResult {

    @ApiModelProperty(value = "是否有停车位")
    private Boolean isSurplus;

    @ApiModelProperty(value = "停车位id")
    private Long parkingId;

    @ApiModelProperty(value = "停车时间id")
    private Long parkingTimeId;
}
