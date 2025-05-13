package com.autumn.common.sensitive.annotation;

import com.autumn.common.sensitive.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

/**
 * @author autumn
 * @desc 自定义脱敏注解
 * @date 2025年05月13日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveData {
    // 脱敏类型
    SensitiveTypeEnum type();
}