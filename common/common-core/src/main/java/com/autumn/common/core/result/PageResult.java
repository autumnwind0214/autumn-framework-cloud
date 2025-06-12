package com.autumn.common.core.result;

import lombok.Data;

import java.util.List;

/**
 * @author 统一分页结果
 */
@Data
public class PageResult<T> {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码(从1开始)
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 数据列表
     */
    private List<T> list;
}
