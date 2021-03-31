package cn.oreo.common.security.starter.configure;

import cn.oreo.common.core.entity.constant.OreoConstant;
import cn.oreo.common.core.utils.OreoUtil;
import cn.oreo.common.security.starter.handler.OreoAccessDeniedHandler;
import cn.oreo.common.security.starter.handler.OreoAuthExceptionEntryPoint;
import cn.oreo.common.security.starter.properties.OreoCloudSecurityProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.Base64Utils;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(OreoCloudSecurityProperties.class)
@ConditionalOnProperty(value = "oreo.cloud.security.enable", havingValue = "true", matchIfMissing = true)
public class OreoCloudSecurityAutoConfigure extends GlobalMethodSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public OreoAccessDeniedHandler accessDeniedHandler() {
        return new OreoAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public OreoAuthExceptionEntryPoint authenticationEntryPoint() {
        return new OreoAuthExceptionEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OreoCloudSecurityInteceptorConfigure OreoCloudSecurityInteceptorConfigure() {
        return new OreoCloudSecurityInteceptorConfigure();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(DefaultTokenServices.class)
    public OreoUserInfoTokenServices OreoUserInfoTokenServices(ResourceServerProperties properties) {
        return new OreoUserInfoTokenServices(properties.getUserInfoUri(), properties.getClientId());
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            String gatewayToken = new String(Base64Utils.encode(OreoConstant.GATEWAY_TOKEN_VALUE.getBytes()));
            requestTemplate.header(OreoConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
            String authorizationToken = OreoUtil.getCurrentTokenValue();
            if (StringUtils.isNotBlank(authorizationToken)) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, OreoConstant.OAUTH2_TOKEN_TYPE + authorizationToken);
            }
        };
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
