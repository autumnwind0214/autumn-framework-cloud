package com.autumn.common.sensitive.aspect;

import com.autumn.common.core.result.R;
import com.autumn.common.sensitive.annotation.SensitiveData;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author autumn
 * @desc 字段脱敏切面
 * @date 2025年05月13日
 */
@Aspect
@Component
public class SensitiveDataAspect {

    @AfterReturning(pointcut = "@annotation(com.autumn.common.sensitive.annotation.Sensitive) " +
            "&& execution(* com.autumn.*.controller.*.*(..))",
            returning = "result")
    public void doAfterReturning(Object result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (result == null) {
            return;
        }
        Object data = null;
        if (result instanceof R res) {
            data = res.getData();
        }

        if (data == null) {
            return;
        }

        if ("com.baomidou.mybatisplus.extension.plugins.pagination.Page".equals(data.getClass().getName())) {
            Object records = data.getClass().getMethod("getRecords").invoke(data);
            if (records instanceof Iterable) {
                for (Object obj : (Iterable<?>) records) {
                    handleSensitiveData(obj);
                }
            }
        } else if (data instanceof Iterable) {
            // 如果返回的是集合，遍历每个对象
            for (Object obj : (Iterable<?>) data) {
                // 对每个对象进行脱敏处理
                handleSensitiveData(obj);
            }
        } else {
            // 对单个对象进行脱敏处理
            handleSensitiveData(data);
        }
    }

    private void handleSensitiveData(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }
        // 获取对象的属性
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(SensitiveData.class)) {
                field.setAccessible(true);
                SensitiveData sensitiveData = field.getAnnotation(SensitiveData.class);
                String value = (String) field.get(obj);

                if (value != null) {
                    switch (sensitiveData.type()) {
                        case PHONE -> field.set(obj, desensitizePhone(value));
                        case ID_CARD -> field.set(obj, desensitizeIdCard(value));
                        case EMAIL -> field.set(obj, desensitizeEmail(value));
                        case NAME -> field.set(obj, desensitizeName(value));
                    }
                }
            }
        }
    }

    private String desensitizePhone(String value) {
        // 手机号脱敏：保留前3后4，如138****8888
        return value.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    private String desensitizeIdCard(String value) {
        // 身份证脱敏：保留前1后1（兼容15位和18位），如1**************X
        return value.replaceAll("(?<=\\d{1})\\d+(?=\\d{1})", "*".repeat(value.length() - 2));
    }

    private String desensitizeEmail(String value) {
        // 邮箱脱敏：保留@前第一个字符和域名，如a***@example.com
        return value.replaceAll("(?<=.)[^@]*(?=@)", "***");
    }

    private String desensitizeName(String value) {
        // 中文姓名脱敏：复姓保留前2后1，单姓保留前1后1，如欧阳*、张*
        if (value.length() >= 3) {
            return value.replaceAll("(?<=[\u4e00-\u9fa5]{2})[\u4e00-\u9fa5]+(?=[\u4e00-\u9fa5]{1})", "*");
        }
        return value.replaceAll("(?<=[\u4e00-\u9fa5]{1})[\u4e00-\u9fa5]+", "*");
    }

}
