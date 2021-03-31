package cn.oreo.auth.configure;

import cn.oreo.auth.filter.ValidateCodeFilter;
import cn.oreo.auth.handler.OreoWebLoginFailureHandler;
import cn.oreo.auth.handler.OreoWebLoginSuccessHandler;
import cn.oreo.common.core.entity.constant.EndpointConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * WebSecurity安全配置
 *
 * @Order(X) 指定执行顺序，值越小，越先执行
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Order(2)
@EnableWebSecurity
@RequiredArgsConstructor
public class OreoSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailService;
    private final ValidateCodeFilter validateCodeFilter;
    private final PasswordEncoder passwordEncoder;
    private final OreoWebLoginSuccessHandler successHandler;
    private final OreoWebLoginFailureHandler failureHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 添加验证码过滤器和用户密码验证过滤器
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .requestMatchers()
                // 开放授权和登陆URL
                .antMatchers(EndpointConstant.OAUTH_ALL, EndpointConstant.LOGIN)
                .and()
                .authorizeRequests()
                .antMatchers(EndpointConstant.OAUTH_ALL).authenticated()
                .and()
                // 登陆配置
                .formLogin()
                .loginPage(EndpointConstant.LOGIN)
                .loginProcessingUrl(EndpointConstant.LOGIN)
                // 成功/失败处理机制
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
                // 关闭 csrf
                .and().csrf().disable()
                // 关闭 httpBasic
                .httpBasic().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }
}
