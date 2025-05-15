package com.autumn.common.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author autumn
 * @desc 消息确认配置
 * @date 2025年05月07日
 */
@Slf4j
@Configuration
public class RabbitConfirmConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送到交换机成功: correlationData={}", correlationData);
        } else {
            log.error("消息发送到交换机失败: correlationData={}, cause={}", correlationData, cause);
            // 处理失败逻辑，如重试、告警等
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息路由到队列失败: exchange={}, routingKey={}, replyCode={}, replyText={}, message={}",
                returned.getExchange(),
                returned.getRoutingKey(),
                returned.getReplyCode(),
                returned.getReplyText(),
                new String(returned.getMessage().getBody()));
        // 处理失败逻辑，如重试、告警等
    }
}

