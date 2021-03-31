package cn.oreo.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 属性注释在数据库表中的注释
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@TableName("t_user_connection")
public class UserConnection {

    @TableField(value = "USER_NAME")
    @NotBlank(message = "{required}")
    private String userName;

    @TableField(value = "PROVIDER_NAME")
    @NotBlank(message = "{required}")
    private String providerName;

    @TableField(value = "PROVIDER_USER_ID")
    @NotBlank(message = "{required}")
    private String providerUserId;

    @TableField(value = "PROVIDER_USER_NAME")
    private String providerUserName;

    @TableField(value = "NICK_NAME")
    private String nickName;

    @TableField("IMAGE_URL")
    private String imageUrl;

    @TableField("LOCATION")
    private String location;

    @TableField("REMARK")
    private String remark;

}