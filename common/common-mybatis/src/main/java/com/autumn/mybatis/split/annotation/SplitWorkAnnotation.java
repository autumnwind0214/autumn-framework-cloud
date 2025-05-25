package com.autumn.mybatis.split.annotation;

import com.autumn.mybatis.split.enums.ThreadPoolEnum;
import com.autumn.mybatis.split.handle.HandleReturn;
import com.autumn.mybatis.split.handle.impl.MergeFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author autumn
 * @desc 切面注解
 * @date 2025/5/25 15:33
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitWorkAnnotation {

    /**
     * 设置线程池
     */
    ThreadPoolEnum setThreadPool();

    /**
     * 返回值处理
     */
    Class<? extends HandleReturn<?>> handlerReturnClass() default MergeFunction.class;

    /**
     * 超过多少开始拆分
     */
    int splitLimit()default 1000;

    /**
     * 拆分后每组多少
     */
    int splitGroupNum()default 100;
}
