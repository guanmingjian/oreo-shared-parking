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
 * @author GuanMingJian
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_car")
@ApiModel(value="Car对象", description="")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "车主id（用户id）")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "车主名字")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "车辆品牌型号")
    @TableField("car_brand")
    private String carBrand;

    @ApiModelProperty(value = "车牌号码")
    @TableField("car_number")
    private String carNumber;

    @ApiModelProperty(value = "车辆类型")
    @TableField("car_type")
    private String carType;

    @ApiModelProperty(value = "注册时间")
    @TableField("register_time")
    private Date registerTime;

    @ApiModelProperty(value = "可用状态")
    @TableField("deleted")
    private Integer deleted;


}
