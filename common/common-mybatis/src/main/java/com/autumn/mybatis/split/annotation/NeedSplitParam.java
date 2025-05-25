package com.autumn.mybatis.split.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author autumn
 * @desc 标记需要拆分参数的注解
 * @date 2025/5/25 15:38
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NeedSplitParam {
}
