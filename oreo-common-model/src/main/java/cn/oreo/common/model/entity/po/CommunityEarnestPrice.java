package cn.oreo.common.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_community_earnest_price")
@ApiModel(value="CommunityEarnestPrice对象", description="")
public class CommunityEarnestPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("community_id")
    private Long communityId;

    @ApiModelProperty(value = "担保金金额")
    @TableField("earnest_price")
    private BigDecimal earnestPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "新增时间")
    @TableField("add_time")
    private Date addTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "编辑时间")
    @TableField("update_time")
    private Date updateTime;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

}
