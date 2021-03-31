package cn.oreo.common.model.entity.dto.server.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author GuanMingJian
 * @since 2020/11/10
 */
@Data
public class OrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "小区id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long communityId;

    @ApiModelProperty(value = "冗余小区名字")
    private String community_name;

    @NotNull
    @ApiModelProperty(value = "停车位id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parkingId;

    @ApiModelProperty(value = "冗余停车位编号")
    private String parkingSn;

    @NotNull
    @ApiModelProperty(value = "车辆id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carId;

    @ApiModelProperty(value = "冗余车辆车牌号")
    private String carNumber;

    @NotNull
    @ApiModelProperty(value = "预约入场时间")
    private Integer enterTime;

    @NotNull
    @ApiModelProperty(value = "预约出场时间")
    private Integer leaveTime;
}
