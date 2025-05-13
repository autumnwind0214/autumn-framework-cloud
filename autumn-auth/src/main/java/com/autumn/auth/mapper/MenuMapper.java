package com.autumn.auth.mapper;


import com.autumn.auth.entity.Menu;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author autumn
 * @desc MenuMapper
 * @date 2024年11月15日
 */
@Mapper
public interface MenuMapper extends BaseMapperPlus<Menu, MenuVo> {
    // 更新deep
    void syncTreeDeep();

    // 更新has_children
    void syncTreeHasChildren();

    List<Long> selectMenuAndChildrenIds(List<Long> ids);

    void updateMenuAndChildrenIsDelete(List<Long> list);

    List<MenuVo> list(MenuDto dto);
}
