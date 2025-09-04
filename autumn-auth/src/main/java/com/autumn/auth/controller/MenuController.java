package com.autumn.auth.controller;

import com.autumn.auth.config.RabbitMqConfig;
import com.autumn.auth.model.dto.MenuCheckDto;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.DynamicRouteVo;
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
    public List<DynamicRouteVo> getAsyncRoutes() {
        Long userId = SecurityUtils.getCurrentUserId();
        return menuService.getAsyncRoutes(userId);
    }

    // 列表查询
    @PreAuthorize("hasRole('admin') || hasAuthority('System:Menu:List')")
    @GetMapping("/list")
    public List<MenuVo> list() {
        return menuService.getList();
    }

    // 新增
    @PreAuthorize("hasRole('admin') || hasAuthority('System:Menu:Create')")
    @PostMapping
    public Boolean add(@RequestBody MenuDto dto) {
        Boolean result = menuService.addMenu(dto);
        messageProducer.sendMessage("菜单路由新增", RabbitMqConfig.MENU_EXCHANGE, RabbitMqConfig.MENU_KEY);
        return result;
    }

    // 编辑
    @PreAuthorize("hasRole('admin') || hasAuthority('System:Menu:Edit')")
    @PutMapping
    public Boolean edit(@RequestBody MenuDto dto) {
        Boolean result = menuService.updateMenu(dto);
        messageProducer.sendMessage("菜单路由更新", RabbitMqConfig.MENU_EXCHANGE, RabbitMqConfig.MENU_KEY);
        return result;
    }

    // 详情
    @GetMapping("/{id}")
    public MenuVo getMenu(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    // 删除
    @PreAuthorize("hasRole('admin') || hasAuthority('System:Menu:Delete')")
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        messageProducer.sendMessage("菜单路由删除", RabbitMqConfig.MENU_EXCHANGE, RabbitMqConfig.MENU_KEY);
        return menuService.delete(id);
    }

    @GetMapping("/nameExists")
    public Boolean nameExists(@RequestParam(required = false) Long menuId, @RequestParam String name) {
        return menuService.checkNameUnique(menuId, name);
    }

    @GetMapping("/pathExists")
    public Boolean pathExists(@RequestParam(required = false) Long menuId, @RequestParam String path) {
        return menuService.checkPathUnique(menuId, path);
    }

}
