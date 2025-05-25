package com.autumn.mybatis.core.service.impl;

import com.autumn.mybatis.core.mapper.TempTableMapper;
import com.autumn.mybatis.core.service.ITemTableService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author autumn
 * @desc TODO
 * @date 2025/5/25 16:52
 **/
@Service
@RequiredArgsConstructor
public class TemTableServiceImpl<T> implements ITemTableService<T> {

    private final TempTableMapper tempTableMapper;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Page<T> queryWithTempTable(List<?> params, Page<T> page) {
        // try {
        //     // 1. 创建临时表
        //     tempTableMapper.createTempTable();
        //     tempTableMapper.truncateTempTable();
        //
        //     // 2. 批量插入ID
        //     batchInsertToTempTable(params);
        //
        //     // 3. 构建分页查询
        //     QueryWrapper<YourEntity> queryWrapper = new QueryWrapper<>();
        //     queryWrapper.inSql("id", "SELECT id FROM temp_ids")
        //             .orderBy(true, page.asc(), page.getOrders());
        //
        //     // 4. 执行分页查询
        //     return yourEntityMapper.selectPage(page, queryWrapper);
        // } finally {
        //     // 5. 清理临时表
        //     tempTableMapper.dropTempTable();
        // }
        return null;
    }

    // 批量插入工具方法
    @Override
    public void batchInsertToTempTable(List<?> ids) {
        if (ids == null || ids.isEmpty()) return;

        // 分批插入，避免SQL过长
        int batchSize = 1000;
        for (int i = 0; i < ids.size(); i += batchSize) {
            List<?> batch = ids.subList(i, Math.min(i + batchSize, ids.size()));
            String sql = "INSERT INTO temp_ids (id) VALUES " +
                    batch.stream()
                            .map(id -> "(" + id + ")")
                            .collect(Collectors.joining(","));
            jdbcTemplate.execute(sql);
        }
    }
}
