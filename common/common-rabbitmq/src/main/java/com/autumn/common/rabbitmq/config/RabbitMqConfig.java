package com.autumn.common.rabbitmq.config;

import com.autumn.blog.common.rabbitmq.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author autumn
 * @desc rabbitmq核心配置
 * @date 2025年05月07日
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {

    /**
     * 业务交换机
     */
    @Bean
    public DirectExchange businessExchange() {
        return ExchangeBuilder.directExchange(RabbitMqConstant.BUSINESS_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(RabbitMqConstant.DEAD_LETTER_EXCHANGE)
                .durable(true).build();
    }

    /**
     * 业务队列
     */
    @Bean
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>();
        // 消息过期时间
        args.put("x-message-ttl", 600000);
        // 队列最大长度
        args.put("x-max-length", 1000);
        // 死信交换机
        args.put("x-dead-letter-exchange", RabbitMqConstant.DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", RabbitMqConstant.DEAD_LETTER_KEY);

        return QueueBuilder.durable(RabbitMqConstant.BUSINESS_QUEUE)
                .withArguments(args).build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(RabbitMqConstant.DEAD_LETTER_QUEUE).build();
    }

    /**
     * 业务绑定
     */
    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue())
                .to(businessExchange())
                .with(RabbitMqConstant.BUSINESS_KEY);
    }

    /**
     * 死信绑定
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(RabbitMqConstant.DEAD_LETTER_KEY);
    }

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate配置
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
