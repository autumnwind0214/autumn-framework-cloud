package com.autumn.mybatis.split.handle;

import java.util.List;

/**
 * @author autumn
 * @desc 处理返回结果接口
 * @date 2025/5/25 15:47
 **/
public interface HandleReturn<T> {

    List<Object> handleReturn(List<List<Object>> results);
}
