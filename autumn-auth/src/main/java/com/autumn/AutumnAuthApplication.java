package com.autumn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 授权服务启动类
 *
 * @author autumn
 */
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class AutumnAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutumnAuthApplication.class, args);
    }
}
