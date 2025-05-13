package com.autumn.common.core.base;

import java.util.List;

/**
 * @author autumn
 * @desc 树形接口
 * @date 2025年05月13日
 */
public interface TreeAble<T> {
    Object getId();

    Object getPid();

    Integer getDeep();

    Integer getSort();

    List<T> getChildren();

    void setChildren(List<T> children);
}
