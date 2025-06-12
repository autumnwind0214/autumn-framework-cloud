package com.autumn.service;


import com.autumn.common.core.result.PageResult;
import com.autumn.entity.Article;
import com.autumn.model.dto.ArticleDto;

/**
 * @author autumn
 * 文章服务接口
 */
public interface IArticleService {
    PageResult<Article> listPage(ArticleDto dto);

    Boolean add(ArticleDto dto);

    Boolean edit(ArticleDto dto);

    Boolean delete(Long[] ids);
}
