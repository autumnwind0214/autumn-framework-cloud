package com.autumn.mybatis.core.model;

import lombok.Data;

/**
 * 分页查询基础类
 *
 * @author autumn
 */
@Data
public class PageQuery {

    {
        page = 1;
        size = 10;
    }

    // 页数
    private Integer page;

    // 每页条数
    private Integer size;
}
