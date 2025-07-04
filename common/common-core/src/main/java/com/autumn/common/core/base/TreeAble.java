package com.autumn.common.core.base;

import java.util.List;

/**
 * 树形接口
 * @author autumn
 */
public interface TreeAble<T> {
    Object getId();

    Object getPid();

    Integer getDeep();

    Integer getSort();

    List<T> getChildren();

    void setChildren(List<T> children);
}
