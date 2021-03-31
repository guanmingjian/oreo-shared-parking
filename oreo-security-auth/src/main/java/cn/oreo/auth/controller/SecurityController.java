package cn.oreo.auth.controller;

import cn.oreo.auth.manager.UserManager;
import cn.oreo.auth.service.ValidateCodeService;
import cn.oreo.common.core.exception.ValidateCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final ValidateCodeService validateCodeService;
    private final UserManager userManager;

    /**
     * 获取用户权限信息
     * @param principal
     * @return
     */
    @ResponseBody
    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    /**
     * 生成验证码
     * @param request
     * @param response
     * @throws IOException
     * @throws ValidateCodeException
     */
    @ResponseBody
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }
}
