package cn.oreo.common.model.entity.dto.server.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuanMingJian
 * @since 2021/2/10
 */
@Data
public class CommunityOption {

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "区域")
    private String area;
}
