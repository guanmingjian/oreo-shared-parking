package cn.oreo.common.model.entity.dto.server.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommunityQuery {

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;
}
