package cn.oreo.common.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_oss")
@ApiModel(value="Oss对象", description="")
public class OssFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "路径地址")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "分类")
    @TableField("sort")
    private String sort;

    @ApiModelProperty(value = "新增时间")
    @TableField("add_time")
    private Date addTime;


}
