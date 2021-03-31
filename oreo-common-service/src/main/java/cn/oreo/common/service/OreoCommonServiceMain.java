package cn.oreo.common.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.oreo.common.service.mapper")
public class OreoCommonServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoCommonServiceMain.class, args);
    }

}
