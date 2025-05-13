package com.autumn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author autumn
 * @desc 授权服务启动类
 * @date 2025年05月07日
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AutumnAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutumnAuthApplication.class, args);
    }
}
