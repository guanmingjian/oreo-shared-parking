package cn.oreo.common.model.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2021-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_community")
@ApiModel(value="Community对象", description="")
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区主键id")
    // type= IdType.INPUT 自定义输入主键id
    @TableId(value = "id",type= IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "小区名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "停车位总数量")
    @TableField("space_total_number")
    private Integer spaceTotalNumber;

    @ApiModelProperty(value = "停车位可用数量")
    @TableField("space_available_number")
    private Integer spaceAvailableNumber;

    @ApiModelProperty(value = "添加时间")
    @TableField("add_time")
    private Date addTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "合作时间")
    @TableField("cooperation_time")
    private Date cooperationTime;

    @ApiModelProperty(value = "合作截至时间")
    @TableField("cooperation_deadline")
    private Date cooperationDeadline;

    @ApiModelProperty(value = "可用状态")
    @TableField("deleted")
    private Integer deleted;

    @ApiModelProperty(value = "省份")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "城市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "区域")
    @TableField("area")
    private String area;

    @ApiModelProperty(value = "详细地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @TableField("latitude")
    private String latitude;


}
