package com.autumn.auth.controller;


import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.RouteVo;
import com.autumn.auth.service.IMenuService;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.result.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author autumn
 * @desc
 * @date 2024年11月15日
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final IMenuService menuService;



    // 获取动态路由
    @GetMapping("/getAsyncRoutes")
    public R<List<RouteVo>> getAsyncRoutes() {
        // Long userId = SecurityUtils.getCurrentUserId();
        Long userId = 1L;
        List<RouteVo> asyncRoutes = menuService.getAsyncRoutes(userId);
        return R.success(asyncRoutes);
    }

    // 列表查询
    @GetMapping("/list")
    public R<List<MenuVo>> list() {
        return R.success(menuService.getList());
    }

    // 新增
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public R<Boolean> add(@RequestBody MenuDto dto) {
        Boolean result = menuService.addMenu(dto);
        menuService.updateRoutesCache();
        return R.success(result);
    }

    // 编辑
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping
    public R<Boolean> edit(@RequestBody MenuDto dto) {
        Boolean result = menuService.updateMenu(dto);
        menuService.updateRoutesCache();
        return R.success(result);
    }

    // 详情
    @GetMapping("/{id}")
    public R<MenuVo> getMenu(@PathVariable Long id) {
        return R.success(menuService.getMenu(id));
    }

    // 删除
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        menuService.delete(id);
        menuService.updateRoutesCache();
        return R.success(true);
    }

}
