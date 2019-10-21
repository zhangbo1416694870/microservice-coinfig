package cn.microservice.config.ui.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.didispace.scca.core.service.PersistenceService;
import com.didispace.scca.core.service.UrlMakerService;

import cn.microservice.config.ui.redirect.DbPersistenceRedirectService;
import cn.microservice.config.ui.redirect.UrlMaker4Eureka;

/**
 *Ui的相关配置
 * @author zhangbo
 * @since 1.0.0
 */
@Configuration
public class UiConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public UrlMakerService getUrlMakerService() {
        return new UrlMaker4Eureka();
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistenceService getPersistenceService() {
        return new DbPersistenceRedirectService();
    }
}
