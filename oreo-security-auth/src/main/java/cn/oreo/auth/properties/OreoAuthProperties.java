package cn.oreo.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:oreo-auth.properties"})
@ConfigurationProperties(prefix = "oreo.auth")
public class OreoAuthProperties {
    /**
     * 验证码配置
     */
    private OreoValidateCodeProperties code = new OreoValidateCodeProperties();
    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;
    /**
     * 是否使用 JWT令牌
     */
    private Boolean enableJwt;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;
}
