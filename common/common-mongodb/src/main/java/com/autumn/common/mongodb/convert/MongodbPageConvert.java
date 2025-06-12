package com.autumn.common.mongodb.convert;

import com.autumn.common.core.result.PageResult;
import org.springframework.data.domain.Page;

/**
 * mongodb分页转换
 * @author autumn
 */
public class MongodbPageConvert {

    public static <T> PageResult<T> convert(Page<T> pageResult) {
        // pageResult.
        PageResult<T> result = new PageResult<>();
        result.setPageSize(pageResult.getSize());
        result.setTotal(pageResult.getTotalElements());
        result.setPageNum(pageResult.getNumber());
        result.setList(pageResult.getContent());
        return result;
    }
}
