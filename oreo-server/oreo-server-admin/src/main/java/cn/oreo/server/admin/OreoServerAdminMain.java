package cn.oreo.server.admin;

import cn.oreo.common.security.starter.annotation.EnableOreoCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"cn.oreo.common.util.exception","cn.oreo.server.admin"})
@EnableOreoCloudResourceServer
@MapperScan("cn.oreo.server.admin.mapper")
public class OreoServerAdminMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoServerAdminMain.class, args);
    }
}
