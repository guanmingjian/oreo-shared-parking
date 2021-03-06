package cn.oreo.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.oreo.auth.entity.BindUser;
import cn.oreo.auth.entity.UserConnection;
import cn.oreo.auth.manager.UserManager;
import cn.oreo.auth.properties.OreoAuthProperties;
import cn.oreo.auth.service.SocialLoginService;
import cn.oreo.auth.service.UserConnectionService;
import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.constant.GrantTypeConstant;
import cn.oreo.common.core.entity.constant.ParamsConstant;
import cn.oreo.common.core.entity.constant.SocialConstant;
import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.core.entity.system.SystemUser;
import cn.oreo.common.core.exception.OreoException;
import cn.oreo.common.core.utils.OreoUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String NOT_BIND = "not_bind";
    private static final String SOCIAL_LOGIN_SUCCESS = "social_login_success";

    private final UserManager userManager;
    private final AuthRequestFactory factory;
    private final OreoAuthProperties properties;
    private final PasswordEncoder passwordEncoder;
    private final UserConnectionService userConnectionService;
    private final ResourceOwnerPasswordTokenGranter granter;
    private final RedisClientDetailsService redisClientDetailsService;

    @Override
    public AuthRequest renderAuth(String oauthType) throws OreoException {
        return factory.get(getAuthSource(oauthType));
    }

    @Override
    public OreoResponse resolveBind(String oauthType, AuthCallback callback) throws OreoException {
        OreoResponse OreoResponse = new OreoResponse();
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse<?> response = authRequest.login(resolveAuthCallback(callback));
        if (response.ok()) {
            OreoResponse.data(response.getData());
        } else {
            throw new OreoException(String.format("????????????????????????%s", response.getMsg()));
        }
        return OreoResponse;
    }

    @Override
    public OreoResponse resolveLogin(String oauthType, AuthCallback callback) throws OreoException {
        OreoResponse OreoResponse = new OreoResponse();
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse<?> response = authRequest.login(resolveAuthCallback(callback));
        if (response.ok()) {
            AuthUser authUser = (AuthUser) response.getData();
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection == null) {
                OreoResponse.message(NOT_BIND).data(authUser);
            } else {
                SystemUser user = userManager.findByName(userConnection.getUserName());
                if (user == null) {
                    throw new OreoException("???????????????????????????????????????????????????");
                }
                OAuth2AccessToken oAuth2AccessToken = getOauth2AccessToken(user);
                OreoResponse.message(SOCIAL_LOGIN_SUCCESS).data(oAuth2AccessToken);
                OreoResponse.put(USERNAME, user.getUsername());
            }
        } else {
            throw new OreoException(String.format("????????????????????????%s", response.getMsg()));
        }
        return OreoResponse;
    }

    @Override
    public OAuth2AccessToken bindLogin(BindUser bindUser, AuthUser authUser) throws OreoException {
        SystemUser systemUser = userManager.findByName(bindUser.getBindUsername());
        if (systemUser == null || !passwordEncoder.matches(bindUser.getBindPassword(), systemUser.getPassword())) {
            throw new OreoException("??????????????????????????????????????????????????????");
        }
        this.createConnection(systemUser, authUser);
        return this.getOauth2AccessToken(systemUser);
    }

    @Override
    public OAuth2AccessToken signLogin(BindUser registUser, AuthUser authUser) throws OreoException {
        SystemUser user = this.userManager.findByName(registUser.getBindUsername());
        if (user != null) {
            throw new OreoException("????????????????????????");
        }
        String encryptPassword = passwordEncoder.encode(registUser.getBindPassword());
        SystemUser systemUser = this.userManager.registUser(registUser.getBindUsername(), encryptPassword);
        this.createConnection(systemUser, authUser);
        return this.getOauth2AccessToken(systemUser);
    }

    @Override
    public void bind(BindUser bindUser, AuthUser authUser) throws OreoException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection != null) {
                throw new OreoException("??????????????????????????????????????????" + userConnection.getUserName() + "????????????");
            }
            SystemUser systemUser = new SystemUser();
            systemUser.setUsername(username);
            this.createConnection(systemUser, authUser);
        } else {
            throw new OreoException("?????????????????????????????????????????????");
        }
    }

    @Override
    public void unbind(BindUser bindUser, String oauthType) throws OreoException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            this.userConnectionService.deleteByCondition(username, oauthType);
        } else {
            throw new OreoException("?????????????????????????????????????????????");
        }
    }

    @Override
    public List<UserConnection> findUserConnections(String username) {
        return this.userConnectionService.selectByCondition(username);
    }

    private void createConnection(SystemUser systemUser, AuthUser authUser) {
        UserConnection userConnection = new UserConnection();
        userConnection.setUserName(systemUser.getUsername());
        userConnection.setProviderName(authUser.getSource().toString());
        userConnection.setProviderUserId(authUser.getUuid());
        userConnection.setProviderUserName(authUser.getUsername());
        userConnection.setImageUrl(authUser.getAvatar());
        userConnection.setNickName(authUser.getNickname());
        userConnection.setLocation(authUser.getLocation());
        this.userConnectionService.createUserConnection(userConnection);
    }

    private AuthCallback resolveAuthCallback(AuthCallback callback) {
        int stateLength = 3;
        String state = callback.getState();
        String[] strings = StringUtils.splitByWholeSeparatorPreserveAllTokens(state, StringConstant.DOUBLE_COLON);
        if (strings.length == stateLength) {
            callback.setState(strings[0] + StringConstant.DOUBLE_COLON + strings[1]);
        }
        return callback;
    }

    private AuthSource getAuthSource(String type) throws OreoException {
        if (StrUtil.isNotBlank(type)) {
            return AuthSource.valueOf(type.toUpperCase());
        } else {
            throw new OreoException(String.format("????????????%s???????????????", type));
        }
    }

    private boolean isCurrentUser(String username) {
        String currentUsername = OreoUtil.getCurrentUsername();
        return StringUtils.equalsIgnoreCase(username, currentUsername);
    }

    private OAuth2AccessToken getOauth2AccessToken(SystemUser user) throws OreoException {
        final HttpServletRequest httpServletRequest = OreoUtil.getHttpServletRequest();
        httpServletRequest.setAttribute(ParamsConstant.LOGIN_TYPE, SocialConstant.SOCIAL_LOGIN);
        String socialLoginClientId = properties.getSocialLoginClientId();
        ClientDetails clientDetails = null;
        try {
            clientDetails = redisClientDetailsService.loadClientByClientId(socialLoginClientId);
        } catch (Exception e) {
            throw new OreoException("??????????????????????????????Client??????");
        }
        if (clientDetails == null) {
            throw new OreoException("?????????????????????????????????Client");
        }
        Map<String, String> requestParameters = new HashMap<>(5);
        requestParameters.put(ParamsConstant.GRANT_TYPE, GrantTypeConstant.PASSWORD);
        requestParameters.put(USERNAME, user.getUsername());
        requestParameters.put(PASSWORD, SocialConstant.setSocialLoginPassword());

        String grantTypes = String.join(StringConstant.COMMA, clientDetails.getAuthorizedGrantTypes());
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientDetails.getClientId(), clientDetails.getScope(), grantTypes);
        return granter.grant(GrantTypeConstant.PASSWORD, tokenRequest);
    }
}
