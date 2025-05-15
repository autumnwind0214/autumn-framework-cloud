package com.autumn.auth.message;

import com.autumn.auth.config.RabbitMqConfig;
import com.autumn.auth.service.IMenuService;
import com.autumn.common.rabbitmq.constant.RabbitMqConstant;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author autumn
 * @desc 消息处理服务
 * @date 2025年05月07日
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final IMenuService menuService;

    @RabbitListener(queues = RabbitMqConfig.MENU_QUEUE, ackMode = "MANUAL")
    public void handleMenuMessage(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 获取消息内容
            String messageBody = new String(message.getBody());
            log.info("收到消息: message={}, deliveryTag={}", messageBody, deliveryTag);

            // 业务处理
            menuService.updateRoutesCache();

            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("消息处理成功: deliveryTag={}", deliveryTag);
        } catch (Exception e) {
            log.error("消息处理异常: deliveryTag={}, error={}", deliveryTag, e.getMessage());

            // 判断是否重新投递
            if (message.getMessageProperties().getRedelivered()) {
                log.error("消息已重试，拒绝消息: deliveryTag={}", deliveryTag);
                channel.basicReject(deliveryTag, false);
            } else {
                log.info("消息首次处理失败，重新投递: deliveryTag={}", deliveryTag);
                channel.basicNack(deliveryTag, false, true);
            }
        }
    }

    private void processMessage(String message) {
        // 实现具体的业务逻辑
        log.info("处理消息: {}", message);
    }
}

