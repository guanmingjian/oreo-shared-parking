package cn.oreo.common.security.starter.configure;

import cn.oreo.common.security.starter.interceptor.OreoServerProtectInterceptor;
import cn.oreo.common.security.starter.properties.OreoCloudSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class OreoCloudSecurityInteceptorConfigure implements WebMvcConfigurer {

    private OreoCloudSecurityProperties properties;

    @Autowired
    public void setProperties(OreoCloudSecurityProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HandlerInterceptor OreoServerProtectInterceptor() {
        OreoServerProtectInterceptor OreoServerProtectInterceptor = new OreoServerProtectInterceptor();
        OreoServerProtectInterceptor.setProperties(properties);
        return OreoServerProtectInterceptor;
    }

    @Override
    @SuppressWarnings("all")
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(OreoServerProtectInterceptor());
    }
}
