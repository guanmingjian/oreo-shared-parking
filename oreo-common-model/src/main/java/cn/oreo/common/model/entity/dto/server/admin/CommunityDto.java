package cn.oreo.common.model.entity.dto.server.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author GuanMingJian
 * @since 2020/12/3
 */
@Data
public class CommunityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区主键id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "小区名称")
    private String name;

    @ApiModelProperty(value = "停车位总数量")
    private Integer spaceTotalNumber;

    @ApiModelProperty(value = "停车位可用数量")
    private Integer spaceAvailableNumber;

    @ApiModelProperty(value = "合作时间")
    private Date cooperationTime;

    @ApiModelProperty(value = "合作截至时间")
    private Date cooperationDeadline;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "维度")
    private String latitude;
}
