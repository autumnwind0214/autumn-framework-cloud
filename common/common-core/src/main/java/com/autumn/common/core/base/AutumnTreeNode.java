package com.autumn.common.core.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点类
 * @param <T> 节点数据类型
 */
@Data
public class AutumnTreeNode<T> {
    // 节点数据
    private T data;
    // 子节点列表
    private List<AutumnTreeNode<T>> children;

    public AutumnTreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public void addChild(AutumnTreeNode<T> child) {
        this.children.add(child);
    }

}
