package com.autumn.auth.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author autumn
 * @desc 消息发送服务
 * @date 2025年05月07日
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Object message, String exchange, String routingKey) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
            log.info("消息发送成功: message={}, exchange={}, routingKey={}, correlationData={}",
                    message, exchange, routingKey, correlationData);
        } catch (Exception e) {
            log.error("消息发送异常: message={}, exchange={}, routingKey={}, correlationData={}, error={}",
                    message, exchange, routingKey, correlationData, e.getMessage());
            throw new RuntimeException("消息发送失败", e);
        }
    }

    public void sendDelayMessage(Object message, String exchange, String routingKey, Long delayMillis) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor = msg -> {
            msg.getMessageProperties().setHeader("x-delay", delayMillis);
            return msg;
        };

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData);
            log.info("延迟消息发送成功: message={}, exchange={}, routingKey={}, delay={}, correlationData={}",
                    message, exchange, routingKey, delayMillis, correlationData);
        } catch (Exception e) {
            log.error("延迟消息发送异常: message={}, exchange={}, routingKey={}, delay={}, correlationData={}, error={}",
                    message, exchange, routingKey, delayMillis, correlationData, e.getMessage());
            throw new RuntimeException("延迟消息发送失败", e);
        }
    }
}

