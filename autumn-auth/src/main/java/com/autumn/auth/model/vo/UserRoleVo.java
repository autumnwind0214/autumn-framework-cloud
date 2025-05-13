package com.autumn.auth.model.vo;

import lombok.Data;

/**
 * @author autumn
 * @desc 用户角色信息
 * @date 2025/4/12 19:33
 **/
@Data
public class UserRoleVo {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色信息,多个逗号分隔
     */
    private String roleInfos;

    /**
     * 角色ID
     */
    private String roleIds;
}
