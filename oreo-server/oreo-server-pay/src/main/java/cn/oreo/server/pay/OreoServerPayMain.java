package cn.oreo.server.pay;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableFeignClients
@SpringBootApplication
@MapperScan("cn.oreo.server.pay.mapper")
public class OreoServerPayMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoServerPayMain.class,args);
    }

}
