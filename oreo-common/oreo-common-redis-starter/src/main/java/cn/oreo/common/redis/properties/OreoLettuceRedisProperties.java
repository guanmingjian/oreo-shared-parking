package cn.oreo.common.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@ConfigurationProperties(prefix = "oreo.lettuce.redis")
public class OreoLettuceRedisProperties {

    /**
     * 是否开启Lettuce Redis
     */
    private Boolean enable = true;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "OreoLettuceRedisProperties{" +
                "enable=" + enable +
                '}';
    }
}
