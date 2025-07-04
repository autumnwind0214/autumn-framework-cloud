package com.autumn.common.core.utils;

import com.autumn.common.core.base.AutumnTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用的树构建工具类
 * @param <T> 节点数据类型
 * @param <ID> 节点ID类型
 */
public class TreeBuilderUtils<T, ID> {

    /**
     * 构建树形结构
     * @param dataList 扁平化数据列表
     * @param idGetter 获取节点ID的函数
     * @param parentIdGetter 获取父节点ID的函数
     * @param rootId 根节点的父ID（通常为null或特定值）
     * @return 树形结构列表
     */
    public List<AutumnTreeNode<T>> buildTree(List<T> dataList,
                                             Function<T, ID> idGetter,
                                             Function<T, ID> parentIdGetter,
                                             ID rootId) {
        if (dataList == null || dataList.isEmpty()) {
            return new ArrayList<>();
        }

        // 将数据转换为树节点
        List<AutumnTreeNode<T>> nodes = dataList.stream()
                .map(AutumnTreeNode::new)
                .toList();

        // 使用Map存储ID到节点的映射，便于快速查找
        Map<ID, AutumnTreeNode<T>> nodeMap = nodes.stream()
                .collect(Collectors.toMap(
                        node -> idGetter.apply(node.getData()),
                        node -> node
                ));

        // 构建树形结构
        List<AutumnTreeNode<T>> tree = new ArrayList<>();
        for (AutumnTreeNode<T> node : nodes) {
            ID parentId = parentIdGetter.apply(node.getData());
            if (parentId == null || parentId.equals(rootId)) {
                // 根节点
                tree.add(node);
            } else {
                // 找到父节点并添加到其子节点列表
                AutumnTreeNode<T> parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.addChild(node);
                }
            }
        }

        return tree;
    }
}
