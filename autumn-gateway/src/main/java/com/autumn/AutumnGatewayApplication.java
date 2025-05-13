package com.autumn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author autumn
 * @desc 网关服务启动类
 * @date 2025年05月07日
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AutumnGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutumnGatewayApplication.class, args);
    }
}
