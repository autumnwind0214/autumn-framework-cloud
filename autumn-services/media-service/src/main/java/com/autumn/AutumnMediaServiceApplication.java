package com.autumn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author autumn
 * @desc 资源媒体服务启动类
 * @date 2025/5/18 13:58
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class AutumnMediaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutumnMediaServiceApplication.class, args);
    }
}
