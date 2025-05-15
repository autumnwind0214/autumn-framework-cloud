package com.autumn.common.rabbitmq.constant;

/**
 * @author autumn
 * @desc rabbitmq 常量类
 * @date 2025年05月07日
 */
public class RabbitMqConstant {
    // 交换机名称
    public static final String BUSINESS_EXCHANGE = "business:change";
    public static final String DEAD_LETTER_EXCHANGE = "dead:letter:exchange";

    // 队列名称
    public static final String BUSINESS_QUEUE = "business:queue";
    public static final String DEAD_LETTER_QUEUE = "dead:letter:queue";

    // 路由键
    public static final String BUSINESS_KEY = "business:key";
    public static final String DEAD_LETTER_KEY = "dead:letter:key";
}
