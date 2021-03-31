package cn.oreo.auth.configure;

import cn.oreo.auth.properties.OreoAuthProperties;
import cn.oreo.auth.service.impl.RedisAuthenticationCodeService;
import cn.oreo.auth.service.impl.RedisClientDetailsService;
import cn.oreo.auth.translator.OreoWebResponseExceptionTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.UUID;

/**
 * 授权服务器配置
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class OreoAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailService;
    private final OreoWebResponseExceptionTranslator exceptionTranslator;
    private final OreoAuthProperties properties;
    private final RedisAuthenticationCodeService authenticationCodeService;
    private final RedisClientDetailsService redisClientDetailsService;
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 把客户端信息存储到Redis中
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
    }

    /**
     * 令牌访问端点
     * @param endpoints
     */
    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)  // 用户身份认证
                .authorizationCodeServices(authenticationCodeService)   // 授权码服务
                .authenticationManager(authenticationManager)   // 认证管理器
                .exceptionTranslator(exceptionTranslator);  // Web响应异常转换器
        if (properties.getEnableJwt()) {
            endpoints.accessTokenConverter(jwtAccessTokenConverter());  // JWT令牌
        }
    }

    /**
     * 令牌存储策略
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        if (properties.getEnableJwt()) {
            return new JwtTokenStore(jwtAccessTokenConverter());
        } else {
            RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
            // 解决每次生成的 token都一样的问题
            redisTokenStore.setAuthenticationKeyGenerator(oAuth2Authentication -> UUID.randomUUID().toString());
            return redisTokenStore;
        }
    }

    /**
     * Token默认配置
     * @return
     */
    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(redisClientDetailsService);
        return tokenServices;
    }

    /**
     * JWT令牌配置
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) accessTokenConverter.getAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailService);
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        accessTokenConverter.setSigningKey(properties.getJwtAccessKey());
        return accessTokenConverter;
    }

    /**
     * 密码模式配置
     * @param authenticationManager
     * @param oAuth2RequestFactory
     * @return
     */
    @Bean
    public ResourceOwnerPasswordTokenGranter resourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager, OAuth2RequestFactory oAuth2RequestFactory) {
        DefaultTokenServices defaultTokenServices = defaultTokenServices();
        if (properties.getEnableJwt()) {
            defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        }
        return new ResourceOwnerPasswordTokenGranter(authenticationManager, defaultTokenServices, redisClientDetailsService, oAuth2RequestFactory);
    }

    @Bean
    public DefaultOAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(redisClientDetailsService);
    }
}
