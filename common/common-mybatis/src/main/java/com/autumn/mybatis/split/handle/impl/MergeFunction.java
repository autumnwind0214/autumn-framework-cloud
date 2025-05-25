package com.autumn.mybatis.split.handle.impl;

import com.autumn.mybatis.split.handle.HandleReturn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author autumn
 * @desc 集合List等合并策略
 * @date 2025/5/25 15:48
 **/
public abstract class MergeFunction implements HandleReturn<Object> {


    @Override
    public List<Object> handleReturn(List<List<Object>> results) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        List<Object> first = new ArrayList<>(results.get(0));
        for (List<Object> result : results) {
            first.addAll(result);
        }
        return first;
    }
}
