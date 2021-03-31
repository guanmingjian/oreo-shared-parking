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
@TableName("t_unit_fee")
@ApiModel(value="UnitFee对象", description="")
public class UnitFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "费用（半小时为单位）")
    @TableField("unit_fee")
    private BigDecimal unitFee;

    @ApiModelProperty(value = "描述")
    @TableField("unit_description")
    private String unitDescription;

    @TableField("add_time")
    private Date addTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("deleted")
    private Integer deleted;

}
