package com.autumn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author autumn
 * 内容管理服务
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AutumnContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutumnContentServiceApplication.class, args);
    }
}
