package com.autumn.common.rabbitmq.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author autumn
 * @desc 消息追踪
 * @date 2025年05月15日
 */
@Aspect
@Component
@Slf4j
public class MessageTraceAspect {

    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public Object traceMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        String messageId = MDC.get("messageId");
        log.info("开始处理消息: messageId={}", messageId);
        try {
            Object result = joinPoint.proceed();
            log.info("消息处理完成: messageId={}", messageId);
            return result;
        } catch (Exception e) {
            log.error("消息处理异常: messageId={}, error={}", messageId, e.getMessage());
            throw e;
        }
    }
}
