package com.autumn.controller;

import com.autumn.common.core.result.PageResult;
import com.autumn.entity.Article;
import com.autumn.model.dto.ArticleDto;
import com.autumn.model.vo.ArticleVo;
import com.autumn.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author autumn
 * 文章管理控制器
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleService articleService;

    @PostMapping("listPage")
    public PageResult<Article> listPage(@RequestBody ArticleDto dto) {
        return articleService.listPage(dto);
    }

    @PostMapping
    public Boolean add(@RequestBody ArticleDto dto) {
        return articleService.add(dto);
    }

    @PutMapping
    public Boolean edit(@RequestBody ArticleDto dto) {
        return articleService.edit(dto);
    }

    @DeleteMapping("{ids}")
    public Boolean delete(@PathVariable Long[] ids) {
        return articleService.delete(ids);
    }
}
