package cn.oreo.server.parking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author GuanMingJian
 * @since 2020/10/25
 */
@EnableFeignClients
@SpringBootApplication
@MapperScan("cn.oreo.server.parking.mapper")
public class OreoServerParkingMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoServerParkingMain.class, args);
    }
}
