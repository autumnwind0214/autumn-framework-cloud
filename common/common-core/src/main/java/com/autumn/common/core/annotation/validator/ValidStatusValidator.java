package com.autumn.common.core.annotation.validator;

import com.autumn.common.core.annotation.ValidStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

/**
 * @author autumn
 * @desc 自定义状态值校验器
 * @date 2025年05月14日
 */
public class ValidStatusValidator implements ConstraintValidator<ValidStatus, Integer> {
    private final Set<Integer> allowedValues = new HashSet<>();

    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        // 初始化允许的值列表
        for (int value : constraintAnnotation.allowedValues()) {
            allowedValues.add(value);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && allowedValues.contains(value);
    }
}
