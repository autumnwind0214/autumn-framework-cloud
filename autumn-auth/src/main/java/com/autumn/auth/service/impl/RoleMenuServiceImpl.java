package com.autumn.auth.service.impl;


import com.autumn.auth.entity.RoleMenu;
import com.autumn.auth.mapper.RoleMenuMapper;
import com.autumn.auth.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author autumn
 * @desc RoleMenuServiceImpl
 * @date 2025年05月02日
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {
}
