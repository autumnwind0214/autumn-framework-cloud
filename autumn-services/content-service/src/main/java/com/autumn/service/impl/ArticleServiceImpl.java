package com.autumn.service.impl;

import com.autumn.common.core.result.PageResult;
import com.autumn.common.core.utils.MapstructUtils;
import com.autumn.common.mongodb.convert.MongodbPageConvert;
import com.autumn.entity.Article;
import com.autumn.model.dto.ArticleDto;
import com.autumn.repository.ArticleRepository;
import com.autumn.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @author autumn
 * 文章服务实现类
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService {

    private final MongoTemplate mongoTemplate;

    private final ArticleRepository articleRepository;


    @Override
    public PageResult<Article> listPage(ArticleDto dto) {
        // mongodb中分页的页码是从0开始，所以要减一
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        Page<Article> page = articleRepository.findByTitleLike(dto.getTitle(), pageable);
        return MongodbPageConvert.convert(page);
    }

    @Override
    public Boolean add(ArticleDto dto) {
        Article article = MapstructUtils.convert(dto, Article.class);
        if (article != null) {
            articleRepository.insert(article);
            return true;
        }
        return false;
    }

    @Override
    public Boolean edit(ArticleDto dto) {
        return null;
    }

    @Override
    public Boolean delete(Long[] ids) {
        return null;
    }
}
