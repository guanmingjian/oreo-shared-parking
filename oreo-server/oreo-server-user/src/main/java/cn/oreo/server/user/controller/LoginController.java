package cn.oreo.server.user.controller;

import cn.oreo.common.model.entity.dto.server.user.login.LoginDto;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.user.service.LoginService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 登录控制器，两种登录方式
 *
 * @author GuanMingJian
 * @since 2020/10/22
 */
@Validated
@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("{loginMethod}")
    public OreoResponse login(
            @Valid LoginDto loginDto,
            @NotNull @PathVariable("loginMethod") @ApiParam("密码登录：password,验证码：captcha,必传") String loginMethod
    ) {

        OreoResponse oreoResponse = loginService.userLogin(loginDto, loginMethod);

        return oreoResponse;
    }

}
