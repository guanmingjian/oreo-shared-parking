package cn.oreo.common.model.entity.po;

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
 * @author XuMin
 * @since 2021-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_parking_time")
@ApiModel(value="ParkingTime对象", description="")
public class ParkingTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "车位id")
    @TableField("parking_id")
    private Long parkingId;

    @ApiModelProperty(value = "停车位所属小区id")
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "车位共享日期")
    @TableField("parking_start_date")
    private Date parkingStartDate;

    @ApiModelProperty(value = "车位共享开始时间")
    @TableField("parking_start_time")
    private Integer parkingStartTime;

    @ApiModelProperty(value = "车位共享结束时间")
    @TableField("parking_end_time")
    private Integer parkingEndTime;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}
