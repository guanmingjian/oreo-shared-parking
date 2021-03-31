package cn.oreo.common.model.entity.dto.server.order;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author GuanMingJian
 * @since 2021/1/20
 */
@Data
public class ReserveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约表主键id，可为空，用于更新时传回")
    private Long reserveId;

    @NotNull
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "小区id")
    private Long communityId;

    @NotNull
    @ApiModelProperty(value = "车牌id")
    private Long carId;

    @ApiModelProperty(value = "冗余车辆车牌号")
    @TableField("car_number")
    private String carNumber;

    @ApiModelProperty(value = "冗余小区名字")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "冗余小区地址")
    @TableField("community_address")
    private String communityAddress;

    @NotNull
    @ApiModelProperty(value = "预约开始时间")
    private Integer reserveStartTime;

    @NotNull
    @ApiModelProperty(value = "预约结束时间")
    private Integer reserveEndTime;

    @NotNull
    @ApiModelProperty(value = "收费方式")
    private Integer rates;

}
