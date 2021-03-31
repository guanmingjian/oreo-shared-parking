package cn.oreo.server.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author GuanMingJian
 * @since 2020/10/25
 */
@EnableTransactionManagement
@EnableFeignClients
@SpringBootApplication
@MapperScan("cn.oreo.server.order.mapper")
public class OreoServerOrderMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoServerOrderMain.class, args);
    }
}
