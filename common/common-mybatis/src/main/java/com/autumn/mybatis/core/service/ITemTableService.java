package com.autumn.mybatis.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author autumn
 * @desc 临时表服务
 * @date 2025/5/25 16:51
 **/
public interface ITemTableService<T> {

    public Page<T> queryWithTempTable(List<?> params, Page<T> page);

    // 批量插入工具方法
    void batchInsertToTempTable(List<?> ids);
}
