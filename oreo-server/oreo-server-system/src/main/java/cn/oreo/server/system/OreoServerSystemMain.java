package cn.oreo.server.system;

import cn.oreo.common.security.starter.annotation.EnableOreoCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@EnableAsync
@SpringBootApplication
@EnableOreoCloudResourceServer
@MapperScan("cn.oreo.server.system.mapper")
public class OreoServerSystemMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(OreoServerSystemMain.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
