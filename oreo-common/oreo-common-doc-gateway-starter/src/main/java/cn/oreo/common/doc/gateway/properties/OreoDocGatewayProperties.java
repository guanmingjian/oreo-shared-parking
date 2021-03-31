package cn.oreo.common.doc.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 */
@ConfigurationProperties(prefix = "oreo.doc.gateway")
public class OreoDocGatewayProperties {

    /**
     * 是否开启网关文档聚合功能
     */
    private Boolean enable;

    /**
     * 需要暴露doc的服务列表，多个值时用逗号分隔
     * 如 OREO-Auth,OREO-Server-System
     */
    private String resources;

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "OreoDocGatewayProperties{" +
                "enable=" + enable +
                ", resources='" + resources + '\'' +
                '}';
    }
}
