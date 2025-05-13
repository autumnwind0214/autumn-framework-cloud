package com.autumn.auth.mapper;

import com.autumn.auth.entity.UserRole;
import com.autumn.auth.model.vo.UserRoleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author autumn
 * @desc UserRoleMapper
 * @date 2025/4/12 19:31
 **/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户id查询菜单id
     * @param userId 用户id
     * @return 菜单id集合
     */
    List<Long> queryMenuIdByUserId(@Param("userId")Long userId);

    /**
     * 根据用户id查询权限
     * @param userId 用户id
     * @return 权限集合
     */
    List<String> queryPermissionByUserId(@Param("userId")Long userId);

    /**
     * 根据用户id查询角色
     * @param userId 用户id
     * @return 角色集合
     */
    List<String> queryRolesByUserId(@Param("userId")Long userId);

    /**
     * 根据用户id查询角色id
     * @param userId 用户id
     * @return 角色id集合
     */
    List<Long> queryRoleIdsByUserId(@Param("userId")Long userId);

    List<UserRoleVo> queryUserRoleInfo(List<Long> userIds);
}
