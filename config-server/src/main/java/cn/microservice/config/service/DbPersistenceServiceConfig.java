package cn.microservice.config.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.microservice.config.service.impl.DbPersistenceServiceImpl;

/**
 * 配置文件数据库持久化自动注入
 * 
 * @author zhangbo
 * @since 1.0.0
 */
@Configuration
public class DbPersistenceServiceConfig {
    /**
     * 数据库持久化操作bean注入.
     *
     * @return a bean of PersistenceService
     * @author zhangbo
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean
    public PersistenceService getPersistenceService() {
        return new DbPersistenceServiceImpl();
    }
}
