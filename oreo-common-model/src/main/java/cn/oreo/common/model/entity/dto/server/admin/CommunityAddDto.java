package cn.oreo.common.model.entity.dto.server.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/12/3
 */
@Data
public class CommunityAddDto{

    @ApiModelProperty(value = "小区名称")
    private String name;

    @ApiModelProperty(value = "停车位总数量")
    private Integer spaceTotalNumber;

    @ApiModelProperty(value = "停车位可用数量")
    private Integer spaceAvailableNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "合作时间")
    private Date cooperationTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    @ApiModelProperty(value = "小区图片路径列表")
    private List<String> communityPhotoUrlList;
}
