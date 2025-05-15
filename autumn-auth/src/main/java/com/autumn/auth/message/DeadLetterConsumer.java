package com.autumn.auth.message;

import com.autumn.common.rabbitmq.constant.RabbitMqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author autumn
 * @desc 死信消息处理
 * @date 2025年05月07日
 */
@Service
@Slf4j
public class DeadLetterConsumer {

    @RabbitListener(queues = RabbitMqConstant.DEAD_LETTER_QUEUE)
    public void handleDeadLetter(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String messageBody = new String(message.getBody());
            log.info("收到死信消息: message={}, deliveryTag={}", messageBody, deliveryTag);

            // 死信消息处理逻辑
            processDeadLetter(messageBody);

            channel.basicAck(deliveryTag, false);
            log.info("死信消息处理成功: deliveryTag={}", deliveryTag);

        } catch (Exception e) {
            log.error("死信消息处理异常: deliveryTag={}, error={}", deliveryTag, e.getMessage());
            channel.basicReject(deliveryTag, false);
        }
    }

    private void processDeadLetter(String message) {
        // 实现死信消息处理逻辑
        log.info("处理死信消息: {}", message);
    }
}

