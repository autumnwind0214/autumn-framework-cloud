package com.autumn.mybatis.core.mapper;

import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;

/**
 * @author autumn
 * @desc 临时表映射接口
 * @date 2025/5/25 15:25
 **/
@Mapper
public interface TempTableMapper {

    @Update("CREATE TEMPORARY TABLE IF NOT EXISTS temp_ids (id BIGINT PRIMARY KEY)")
    void createTempTable();

    @Update("TRUNCATE TABLE temp_ids")
    void truncateTempTable();

    @Update("DROP TABLE IF EXISTS temp_ids")
    void dropTempTable();
}
