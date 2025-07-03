package com.autumn.auth.controller;

import com.autumn.auth.config.RabbitMqConfig;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.RouteVo;
import com.autumn.auth.service.IMenuService;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.result.R;
import com.autumn.auth.message.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author autumn
 * @date 2024年11月15日
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final IMenuService menuService;

    private final MessageProducer messageProducer;

    // 获取动态路由
    @GetMapping("/getAsyncRoutes")
    public List<RouteVo> getAsyncRoutes() {
        Long userId = SecurityUtils.getCurrentUserId();
        return menuService.getAsyncRoutes(userId);
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
        messageProducer.sendMessage("菜单路由新增", RabbitMqConfig.MENU_EXCHANGE, RabbitMqConfig.MENU_KEY);
        return R.success(result);
    }

    // 编辑
    @PreAuthorize("hasRole('admin') || hasAuthority('system:menu:edit')")
    @PutMapping
    public R<Boolean> edit(@RequestBody MenuDto dto) {
        Boolean result = menuService.updateMenu(dto);
        messageProducer.sendMessage("菜单路由更新", RabbitMqConfig.MENU_EXCHANGE, RabbitMqConfig.MENU_KEY);
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
        messageProducer.sendMessage("菜单路由删除", RabbitMqConfig.MENU_EXCHANGE, RabbitMqConfig.MENU_KEY);
        return R.success(true);
    }

}
