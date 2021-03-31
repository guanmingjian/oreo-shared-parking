package cn.oreo.common.doc.gateway.configure;

import cn.oreo.common.doc.gateway.filter.OreoDocGatewayHeaderFilter;
import cn.oreo.common.doc.gateway.handler.OreoDocGatewayHandler;
import cn.oreo.common.doc.gateway.properties.OreoDocGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 */
@Configuration
@EnableConfigurationProperties(OreoDocGatewayProperties.class)
@ConditionalOnProperty(value = "oreo.doc.gateway.enable", havingValue = "true", matchIfMissing = true)
public class OreoDocGatewayAutoConfigure {

    private final OreoDocGatewayProperties oreoDocGatewayProperties;
    private SecurityConfiguration securityConfiguration;
    private UiConfiguration uiConfiguration;

    public OreoDocGatewayAutoConfigure(OreoDocGatewayProperties oreoDocGatewayProperties) {
        this.oreoDocGatewayProperties = oreoDocGatewayProperties;
    }

    @Autowired(required = false)
    public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    @Autowired(required = false)
    public void setUiConfiguration(UiConfiguration uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
    }

    @Bean
    public OreoDocGatewayResourceConfigure oreoDocGatewayResourceConfigure(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        return new OreoDocGatewayResourceConfigure(routeLocator, gatewayProperties);
    }

    @Bean
    public OreoDocGatewayHeaderFilter oreoDocGatewayHeaderFilter() {
        return new OreoDocGatewayHeaderFilter();
    }

    @Bean
    public OreoDocGatewayHandler oreoDocGatewayHandler(SwaggerResourcesProvider swaggerResources) {
        OreoDocGatewayHandler oreoDocGatewayHandler = new OreoDocGatewayHandler();
        oreoDocGatewayHandler.setSecurityConfiguration(securityConfiguration);
        oreoDocGatewayHandler.setUiConfiguration(uiConfiguration);
        oreoDocGatewayHandler.setSwaggerResources(swaggerResources);
        oreoDocGatewayHandler.setProperties(oreoDocGatewayProperties);
        return oreoDocGatewayHandler;
    }
}
