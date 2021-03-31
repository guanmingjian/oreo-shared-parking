package cn.oreo.monitor.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author GuanMingJian
 */
@EnableAdminServer
@SpringBootApplication
public class OreoAdminMain {

    public static void main(String[] args) {
        SpringApplication.run(OreoAdminMain.class, args);
    }

}
