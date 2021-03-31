package cn.oreo.common.model.entity.dto.server.user.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登录交互类
 *
 * @author GuanMingJian
 * @since 2020/10/23
 */
@Data
public class LoginDto {

    @NotNull
    @ApiModelProperty(value = "必传")
    private String phoneNumber;

    private String password;

    /**
     * 手机验证码
     */
    private String captcha;
}
