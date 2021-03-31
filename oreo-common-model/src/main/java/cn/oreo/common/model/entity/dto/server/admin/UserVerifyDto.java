package cn.oreo.common.model.entity.dto.server.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
@Data
public class UserVerifyDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "小区id")
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "冗余小区名称")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "停车位编号")
    @TableField("parking_sn")
    private String parkingSn;

    @ApiModelProperty(value = "产权人名字")
    @TableField("owner_name")
    private String ownerName;

    @ApiModelProperty(value = "身份证号码")
    @TableField("id_card")
    private String idCard;

    @ApiModelProperty(value = "证明材料图片路径")
    @TableField("probative_url")
    private String probativeUrl;

}
