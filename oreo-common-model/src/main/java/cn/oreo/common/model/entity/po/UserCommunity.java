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
 * @since 2021-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_community")
@ApiModel(value="UserCommunity对象", description="")
public class UserCommunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "小区id")
    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "新增时间")
    @TableField("add_time")
    private Date addTime;


}
