package com.autumn.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.Collection;

/**
 * @author autumn
 * @desc 自定义 Mapper 接口, 实现 自定义扩展
 * @date 2025年05月08日
 */
public interface BaseMapperPlus<T, V> extends BaseMapper<T> {

    Log log = LogFactory.getLog(BaseMapperPlus.class);

    /**
     * 批量插入实体对象集合
     *
     * @param entityList 实体对象集合
     * @return 插入操作是否成功的布尔值
     */
    int insertBatchSomeColumn(Collection<T> entityList);
}
