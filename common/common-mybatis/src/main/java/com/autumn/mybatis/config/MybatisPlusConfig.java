package com.autumn.mybatis.config;

import com.autumn.mybatis.core.injector.MySqlInjector;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;

/**
 * @author autumn
 * MybatisPlus配置类
 */
@Slf4j
@EnableTransactionManagement
@Configuration
@MapperScan("com.autumn.**.mapper")
public class MybatisPlusConfig {

    @Bean
    public MySqlInjector mySqlInjector() {
        return new MySqlInjector();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        // 初始化 MybatisPlusInterceptor 核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加自动分页插件 PaginationInnerInterceptor
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 返回
        return interceptor;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator(getWorkerId(), getDatacenterId());
    }

    /**
     * 获取workerId, 优先从环境变量中获取，否则通过IP计算
     */
    private Long getWorkerId() {
        try {
            String workerIdStr = System.getenv("WORKER_ID");
            if (workerIdStr != null) {
                return Long.parseLong(workerIdStr);
            }
            // 获取本机IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
            int ipLastSegment = Integer.parseInt(ip.split("\\.")[3]);
            return (long) (ipLastSegment % 32);
        } catch (Exception e) {
            log.error("Get workId failed, fallback to default 1", e);
            return 1L;
        }
    }

    /**
     * 获取数据中心Id, 优先从环境变量中获取，否则通过IP计算
     */
    private Long getDatacenterId() {
        try {
            String datacenterIdStr = System.getenv("DATACENTER_ID");
            if (datacenterIdStr != null) {
                return Long.parseLong(datacenterIdStr);
            }
            // 获取本机IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
            int ipLastSegment = Integer.parseInt(ip.split("\\.")[3]);
            return (long) (ipLastSegment % 32);
        } catch (Exception e) {
            log.error("Get datacenterId failed, fallback to default 1", e);
            return 1L;
        }
    }

}
