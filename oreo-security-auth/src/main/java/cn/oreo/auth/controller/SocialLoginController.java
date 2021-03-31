package cn.oreo.auth.controller;

import cn.oreo.auth.entity.BindUser;
import cn.oreo.auth.entity.UserConnection;
import cn.oreo.auth.service.SocialLoginService;
import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.core.exception.OreoException;
import cn.oreo.common.core.utils.OreoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("social")
public class SocialLoginController {

    private static final String TYPE_LOGIN = "login";
    private static final String TYPE_BIND = "bind";

    private final SocialLoginService socialLoginService;

    @Value("${oreo.frontUrl}")
    private String frontUrl;

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     */
    @ResponseBody
    @GetMapping("/login/{oauthType}/{type}")
    public void renderAuth(@PathVariable String oauthType, @PathVariable String type, HttpServletResponse response) throws IOException, OreoException {
        AuthRequest authRequest = socialLoginService.renderAuth(oauthType);
        response.sendRedirect(authRequest.authorize(oauthType + StringConstant.DOUBLE_COLON + AuthStateUtils.createState()) + "::" + type);
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return String
     */
    @GetMapping("/{oauthType}/callback")
    public String login(@PathVariable String oauthType, AuthCallback callback, String state, Model model) {
        try {
            OreoResponse OreoResponse = null;
            String type = StringUtils.substringAfterLast(state, StringConstant.DOUBLE_COLON);
            if (StringUtils.equals(type, TYPE_BIND)) {
                OreoResponse = socialLoginService.resolveBind(oauthType, callback);
            } else {
                OreoResponse = socialLoginService.resolveLogin(oauthType, callback);
            }
            model.addAttribute("response", OreoResponse);
            model.addAttribute("frontUrl", frontUrl);
            return "result";
        } catch (Exception e) {
            String errorMessage = OreoUtil.containChinese(e.getMessage()) ? e.getMessage() : "第三方登录失败";
            model.addAttribute("error", e.getMessage());
            return "fail";
        }
    }

    /**
     * 绑定并登录
     *
     * @param bindUser bindUser
     * @param authUser authUser
     * @return OreoResponse
     */
    @ResponseBody
    @PostMapping("bind/login")
    public OreoResponse bindLogin(@Valid BindUser bindUser, AuthUser authUser) throws OreoException {
        OAuth2AccessToken oAuth2AccessToken = this.socialLoginService.bindLogin(bindUser, authUser);
        return new OreoResponse().data(oAuth2AccessToken);
    }

    /**
     * 注册并登录
     *
     * @param registUser registUser
     * @param authUser   authUser
     * @return OreoResponse
     */
    @ResponseBody
    @PostMapping("sign/login")
    public OreoResponse signLogin(@Valid BindUser registUser, AuthUser authUser) throws OreoException {
        OAuth2AccessToken oAuth2AccessToken = this.socialLoginService.signLogin(registUser, authUser);
        return new OreoResponse().data(oAuth2AccessToken);
    }

    /**
     * 绑定
     *
     * @param bindUser bindUser
     * @param authUser authUser
     */
    @ResponseBody
    @PostMapping("bind")
    public void bind(BindUser bindUser, AuthUser authUser) throws OreoException {
        this.socialLoginService.bind(bindUser, authUser);
    }

    /**
     * 解绑
     *
     * @param bindUser  bindUser
     * @param oauthType oauthType
     */
    @ResponseBody
    @DeleteMapping("unbind")
    public void unbind(BindUser bindUser, String oauthType) throws OreoException {
        this.socialLoginService.unbind(bindUser, oauthType);
    }

    /**
     * 根据用户名获取绑定关系
     *
     * @param username 用户名
     * @return OreoResponse
     */
    @ResponseBody
    @GetMapping("connections/{username}")
    public OreoResponse findUserConnections(@NotBlank(message = "{required}") @PathVariable String username) {
        List<UserConnection> userConnections = this.socialLoginService.findUserConnections(username);
        return new OreoResponse().data(userConnections);
    }
}
