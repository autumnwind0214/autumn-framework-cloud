package com.autumn.common.core.annotation;

import com.autumn.common.core.annotation.validator.ValidStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author autumn
 * @desc 状态值校验注解
 * @date 2025年05月14日
 */
@Documented
@Constraint(validatedBy = ValidStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {
    String message() default "状态值必须为0或1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 允许的枚举值
    int[] allowedValues() default {0, 1};
}
