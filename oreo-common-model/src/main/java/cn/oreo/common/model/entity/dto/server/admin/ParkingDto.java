package cn.oreo.common.model.entity.dto.server.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author XuMin
 * @since 2021-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ParkingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "停车位所属的用户id",required = true)
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "停车位所属的用户名称")
    @TableField("user_name")
    private String userName;

    @NotNull
    @ApiModelProperty(value = "停车位所属的小区id",required = true)
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "停车位所属的小区名字")
    @TableField("community_name")
    private String communityName;

    @NotNull
    @ApiModelProperty(value = "停车位编号",required = true)
    @TableField("parking_sn")
    private String parkingSn;

    @NotNull
    @ApiModelProperty(value = "车位状态（0：可用 1：禁用）",required = true)
    @TableField("parking_status")
    private String parkingStatus;

}
