package com.autumn.auth.mapper;

import com.autumn.auth.entity.Role;
import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleVo;
import com.autumn.mybatis.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author autumn
 */
@Mapper
public interface RoleMapper extends BaseMapperPlus<Role, RoleVo> {

    Page<RoleVo> listPage(@Param("page") Page<RoleVo> page, @Param("query") RoleDto dto);
}
