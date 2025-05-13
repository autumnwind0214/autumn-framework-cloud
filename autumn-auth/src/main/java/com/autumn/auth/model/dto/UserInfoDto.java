package com.autumn.auth.model.dto;

import com.autumn.mybatis.core.model.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author autumn
 * @desc 用户查询
 * @date 2025/4/20 19:13
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoDto extends PageQuery {

    // 用户名
    private String username;

    // 昵称
    private Integer status;

    // 手机号
    private String phone;
}
