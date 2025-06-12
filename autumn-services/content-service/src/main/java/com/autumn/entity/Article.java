package com.autumn.entity;


import com.autumn.model.dto.ArticleDto;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author autumn
 * 文章
 */
@AutoMappers({
        @AutoMapper(target = ArticleDto.class)
})
@Data
@Document
public class Article {

    /**
     * 文章id
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
}
