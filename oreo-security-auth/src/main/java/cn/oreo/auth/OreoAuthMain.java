package cn.oreo.auth;

import cn.oreo.common.security.starter.annotation.EnableOreoCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableOreoCloudResourceServer
@MapperScan("cn.oreo.auth.mapper")
public class OreoAuthMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(OreoAuthMain.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
