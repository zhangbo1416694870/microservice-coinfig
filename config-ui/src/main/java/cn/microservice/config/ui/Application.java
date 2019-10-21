package cn.microservice.config.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import cn.microservice.config.ui.configuration.UiConfiguration;

/**
 * 配置中心的ui管理微服务启动类.
 * 
 * @author zhangbo
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import(UiConfiguration.class)
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
