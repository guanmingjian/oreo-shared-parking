package cn.oreo.common.model.entity.dto.server.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GuanMingJian
 * @since 2021/2/4
 */
@Data
public class SearchCommunityDto {

    @NotNull
    @ApiModelProperty(value = "省份",required = true)
    private String province;

    @NotNull
    @ApiModelProperty(value = "城市",required = true)
    private String city;

    @NotNull
    @ApiModelProperty(value = "地区",required = true)
    private String area;

    @ApiModelProperty(value = "小区名称(模糊查询)")
    private String communityName;

    @ApiModelProperty(value = "是否需要详细信息列表，需要填true",required = true)
    private Boolean isNeedDetail;
}
