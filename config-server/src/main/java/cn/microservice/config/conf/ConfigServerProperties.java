package cn.microservice.config.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置中心的属性.
 *
 * @author zhangbo
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "server")
public class ConfigServerProperties {
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
