package cn.oreo.gateway;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 * fenzhi
 */
@SpringBootApplication
public class OreoGatewayMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(OreoGatewayMain.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }
}
