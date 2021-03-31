package cn.oreo.server.system.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:oreo-server-system.properties"})
@ConfigurationProperties(prefix = "oreo.server.system")
public class OreoServerSystemProperties {
    /**
     * 批量插入当批次可插入的最大值
     */
    private Integer batchInsertMaxNum = 1000;
}
