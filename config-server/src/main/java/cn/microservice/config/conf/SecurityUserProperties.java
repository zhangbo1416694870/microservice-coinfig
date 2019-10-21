package cn.microservice.config.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * security属性.
 * </p>
 *
 * @author zhangbo
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "security.user")
public class SecurityUserProperties {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
