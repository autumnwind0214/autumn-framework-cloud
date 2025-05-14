package com.autumn.common.core.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author autumn
 * @desc 拷贝工具类
 * @date 2025年05月14日
 */
public class BeanCopyUtils {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    /**
     * list 复制
     *
     * @param sourceList
     * @param targetClass
     * @param <Source>
     * @param <Target>
     * @return
     */
    public static <Source, Target> List<Target> copyList(List<Source> sourceList, Class<Target> targetClass) {
        return sourceList.stream().map(source -> MODEL_MAPPER.map(source, targetClass)).collect(Collectors.toList());
    }

    /**
     * 使用 ModelMapper 进行对象属性复制，采用严格匹配模式。
     * <p>
     * 只有属性名称和类型完全一致的情况下，才会进行属性复制。此方法适用于需要精确映射的场景。
     * </p>
     *
     * @param <T>    目标对象的类型
     * @param source 源对象
     * @param clazz  目标对象的类类型
     * @return 转换后的目标对象实例
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        return MODEL_MAPPER.map(source, clazz);
    }

    public static <T, M> M copy(T source, M target) {
        MODEL_MAPPER.map(source, target);
        return target;
    }

    /**
     * 使用 ModelMapper 进行对象属性复制，严格匹配且不忽略 `null` 值。
     * <p>
     * 此方法在属性名称和类型完全一致时进行复制，即使属性值为 `null` 也会进行覆盖。
     * </p>
     *
     * @param <T>    源对象的类型
     * @param <M>    目标对象的类型
     * @param source 源对象
     * @param target 目标对象
     * @return 无返回值，目标对象的属性会被源对象对应的属性值覆盖
     */
    public static <T, M> M copyNotIgnoreNull(T source, M target) {
        // 不跳过null值
        MODEL_MAPPER.getConfiguration().setSkipNullEnabled(false);
        MODEL_MAPPER.map(source, target);
        return target;
    }
}
