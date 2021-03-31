package cn.oreo.server.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author GuanMingJian
 * @since 2020/10/25
 */
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"cn.oreo.common.util.exception","cn.oreo.server.user"})
@MapperScan("cn.oreo.server.user.mapper")
public class OreoServerUserMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoServerUserMain.class, args);
    }
}
