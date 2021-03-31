package cn.oreo.common.security.starter.configure;

import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.security.starter.handler.OreoAccessDeniedHandler;
import cn.oreo.common.security.starter.handler.OreoAuthExceptionEntryPoint;
import cn.oreo.common.security.starter.properties.OreoCloudSecurityProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@EnableResourceServer
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class OreoCloudResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private OreoCloudSecurityProperties properties;
    private OreoAccessDeniedHandler accessDeniedHandler;
    private OreoAuthExceptionEntryPoint exceptionEntryPoint;

    @Autowired
    public void setProperties(OreoCloudSecurityProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setAccessDeniedHandler(OreoAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    public void setExceptionEntryPoint(OreoAuthExceptionEntryPoint exceptionEntryPoint) {
        this.exceptionEntryPoint = exceptionEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 获取忽略url
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUris(), StringConstant.COMMA);
        if (ArrayUtils.isEmpty(anonUrls)) {
            anonUrls = new String[]{};
        }

        http.csrf().disable()
                .requestMatchers().antMatchers(properties.getAuthUri())
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers(properties.getAuthUri()).authenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
