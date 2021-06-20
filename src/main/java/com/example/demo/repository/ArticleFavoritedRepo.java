package com.example.demo.repository;

import com.example.demo.entity.ArticleFavorite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ArticleFavoritedRepo extends MongoRepository<ArticleFavorite, String> {
    public Set<ArticleFavorite> findArticleFavoriteByUsername(String user);

    public ArticleFavorite findArticleFavoriteByUsernameAndArticleId(String username, String articleId);

    public int countArticleFavoriteByArticleId(String articleId);
}
