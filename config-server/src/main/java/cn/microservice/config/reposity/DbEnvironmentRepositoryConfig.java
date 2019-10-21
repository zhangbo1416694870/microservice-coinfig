package cn.microservice.config.reposity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库作为环境仓库的配置.
 * 
 * @author zhangbo
 * @since 1.0.0
 */
@ConditionalOnProperty(name = "mss.custom.environment.repository", havingValue = "mysql", matchIfMissing = true)
@Configuration
public class DbEnvironmentRepositoryConfig {
    /**
     * bean注入.
     *
     * @return a bean of EnvironmentRepository
     * @author zhangbo
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean
    public EnvironmentRepository getEnvironmentRepository() {
        return new DbEnvironmentRepository();
    }
}
