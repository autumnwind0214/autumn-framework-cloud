package com.autumn.repository;

import com.autumn.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author autumn
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    Page<Article> findByTitleLike(String title, Pageable pageable);
}
