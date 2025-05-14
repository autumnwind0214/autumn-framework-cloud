package com.autumn.auth.service;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.auth.model.dto.ChangePasswordDto;
import com.autumn.auth.model.dto.UserAvatarDto;
import com.autumn.auth.model.dto.UserDto;
import com.autumn.auth.model.dto.UserInfoDto;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author autumn
 * @desc IAuthorizationUserService
 * @date 2025年05月12日
 */
public interface IAuthorizationUserService extends IService<AuthorizationUser> , UserDetailsService {

    AuthorizationUserVo getUserInfo(Long userId);

    Page<AuthorizationUserVo> listPage(UserInfoDto dto);

    Boolean add(UserDto dto);

    Boolean edit(UserDto dto);

    Boolean editStatus(Long id, Integer status);

    Boolean delete(Long[] ids);

    Boolean changePassword(ChangePasswordDto dto);

    Long[] getRoleIds(Long userId);

    Boolean uploadAvatar(UserAvatarDto dto);
}
