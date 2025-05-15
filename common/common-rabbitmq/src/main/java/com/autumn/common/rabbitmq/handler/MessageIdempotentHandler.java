package com.autumn.common.rabbitmq.handler;

import com.autumn.common.redis.core.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author autumn
 * @desc 消息幂等性处理
 * @date 2025年05月07日
 */
@Service
@RequiredArgsConstructor
public class MessageIdempotentHandler {

    private final RedisOperator<String> redisOperator;

    public boolean isProcessed(String messageId) {
        String key = "mq:processed:" + messageId;
        return Boolean.TRUE.equals(redisOperator.setIfAbsent(key, "1", 24, TimeUnit.HOURS));
    }
}

