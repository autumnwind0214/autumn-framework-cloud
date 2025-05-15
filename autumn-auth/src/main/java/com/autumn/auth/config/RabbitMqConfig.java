package com.autumn.auth.config;

import com.autumn.common.rabbitmq.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author autumn
 * @desc rabbitmq配置
 * @date 2025年05月15日
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {

    public static final String MENU_QUEUE = "menu.update.queue";
    public static final String MENU_EXCHANGE = "menu.update.exchange";
    public static final String MENU_KEY = "menu.update.key";

    /**
     * 菜单路由更新交换机
     */
    @Bean
    public DirectExchange menuUpdateExchange() {
        return ExchangeBuilder.directExchange(MENU_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 业务队列
     */
    @Bean
    public Queue menuUpdateQueue() {
        Map<String, Object> args = new HashMap<>();
        // 消息过期时间
        args.put("x-message-ttl", 600000);
        // 队列最大长度
        args.put("x-max-length", 1000);
        // 死信交换机
        args.put("x-dead-letter-exchange", RabbitMqConstant.DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", RabbitMqConstant.DEAD_LETTER_KEY);

        return QueueBuilder.durable(MENU_QUEUE)
                .withArguments(args).build();
    }

    /**
     * 业务绑定
     */
    @Bean
    public Binding businessMenuUpdate() {
        return BindingBuilder.bind(menuUpdateQueue())
                .to(menuUpdateExchange())
                .with(MENU_KEY);
    }

}
