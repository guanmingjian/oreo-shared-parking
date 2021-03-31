package cn.oreo.common.service.configure;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class OssStorageConfig {

    @Value("${oss.ENDPOINT}")
    private String ENDPOINT;

    @Value("${oss.ACCESS_KEY_ID}")
    private String ACCESS_KEY_ID;

    @Value("${oss.ACCESS_KEY_SECRET}")
    private String ACCESS_KEY_SECRET;

    @Value("${oss.BUCKET_NAME}")
    private String BUCKET_NAME;

}
