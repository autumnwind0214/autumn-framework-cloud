package com.autumn.auth.mapper;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.auth.model.dto.UserInfoDto;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.mybatis.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author autumn
 * @desc AuthorizationUserMapper
 * @date 2025年05月12日
 */
@Mapper
public interface AuthorizationUserMapper extends BaseMapperPlus<AuthorizationUser, AuthorizationUserVo> {

    Page<AuthorizationUserVo> listPage(@Param("page") Page<AuthorizationUserVo> page, @Param("query") UserInfoDto dto);
}
