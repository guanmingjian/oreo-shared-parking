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

import javax.validation.constraints.NotNull;

/**
 *
 * @author GuanMingJian
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @NotNull
    @ApiModelProperty(value = "id不可为空")
    private Long id;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone_number")
    private String phoneNumber;

    @ApiModelProperty(value = "昵称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "年龄（当前日期 - 出生日期）")
    @TableField("age")
    private Integer age;

    @ApiModelProperty(value = "性别 0:男 1：女")
    @TableField("gender")
    private String gender;

    @ApiModelProperty(value = "真实姓名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty(value = "头像路径")
    @TableField("avatar_url")
    private String avatarUrl;

    @ApiModelProperty(value = "身份证号码")
    @TableField("id_card")
    private String idCard;

    @ApiModelProperty(value = "注册时间")
    @TableField("register_time")
    private Date registerTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "最后登录时间")
    @TableField("last_login_time")
    private Date lastLoginTime;

    @ApiModelProperty(value = "账户金额")
    @TableField("account_amount")
    private Double accountAmount;

    @ApiModelProperty(value = "信用分")
    @TableField("credit_score")
    private Integer creditScore;

    @ApiModelProperty(value = "用户类型 1：普通用户 2：业主")
    @TableField("user_type")
    private String userType;

    @ApiModelProperty(value = "用户信息完善程度标识符，0：未完善身份信息，1：完善身份信息")
    @TableField("info_code")
    private String infoCode;

    @ApiModelProperty(value = "可用状态 0:可用 1：禁用/被删除")
    @TableField("deleted")
    private Integer deleted;

    @ApiModelProperty(value = "出生日期")
    @TableField("born")
    private String born;

    @ApiModelProperty(value = "用户地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "民族")
    @TableField("nation")
    private String nation;

    @ApiModelProperty(value = "签发日期")
    @TableField("begin_date")
    private String beginDate;

    @ApiModelProperty(value = "签发机关")
    @TableField("department")
    private String department;

    @ApiModelProperty(value = "失效日期")
    @TableField("end_date")
    private String endDate;

    @ApiModelProperty(value = "全局ID")
    @TableField("open_id")
    private String openId;


}
