package com.autumn.common.core.utils;

import com.autumn.common.core.base.AutumnTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
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

    /**
     * 将树形结构映射回数据对象，并填充子节点
     * @param node 当前节点
     * @param childSetter 用于设置子节点的函数
     * @return 数据对象 T
     */
    private T mapToDataObject(AutumnTreeNode<T> node, BiConsumer<T, List<T>> childSetter) {
        T data = node.getData();
        List<T> childList = node.getChildren().stream()
                .map(childNode -> mapToDataObject(childNode, childSetter))
                .toList();
        childSetter.accept(data, childList);
        return data;
    }

    /**
     * 构建树形结构并映射回数据对象
     * @param dataList 扁平化数据列表
     * @param idGetter 获取节点ID的函数
     * @param parentIdGetter 获取父节点ID的函数
     * @param rootId 根节点的父ID
     * @param childSetter 用于设置子节点的函数
     * @return 数据对象列表，每个对象内部包含子节点
     */
    public List<T> buildAndMapToData(List<T> dataList,
                                      Function<T, ID> idGetter,
                                      Function<T, ID> parentIdGetter,
                                      ID rootId,
                                      BiConsumer<T, List<T>> childSetter) {
        List<AutumnTreeNode<T>> tree = buildTree(dataList, idGetter, parentIdGetter, rootId);
        return tree.stream()
                .map(node -> mapToDataObject(node, childSetter))
                .toList();
    }
}
