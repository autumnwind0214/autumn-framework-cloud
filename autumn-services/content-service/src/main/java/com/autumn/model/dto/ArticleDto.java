package com.autumn.model.dto;

import com.autumn.entity.Article;
import com.autumn.mybatis.core.model.PageQuery;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author autumn
 */
@AutoMapper(target = Article.class)
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDto extends PageQuery {

    String title;

    String content;
}
