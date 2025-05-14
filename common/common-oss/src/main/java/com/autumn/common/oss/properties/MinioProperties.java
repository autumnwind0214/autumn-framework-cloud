package com.autumn.common.oss.properties;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author autumn
 * @desc minio配置
 * @date 2025年05月14日
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.minio")
public class MinioProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String mediaBucket;

    public String getMediaUrl() {
        return this.endpoint + "/" + mediaBucket;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
