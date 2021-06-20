package com.example.demo.repository;
import com.example.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    public Page<Article> findAll(Pageable pageable);

    public Page<Article> findArticlesByTitleContaining(String title, Pageable pageable);

    public Page<Article> findArticlesByUsernameAndTitleContaining(String targetname, String title, Pageable pageable);

    public Article findArticlesBySlug(String slug);

    public Article findArticlesById(String articleId);

    public Page<Article> findAllByUsername(String authorName, Pageable pageable);

    public Optional<Article> findById(String id);

    @Query(value = "{'tags.name' : ?0 }")
    public Page<Article> findArticlesByTagList(String tag , Pageable pageable);

    @Query(value = "{ 'username' :'?0' ,'tags.name' : ?1}")
    public List<Article> findArticlesByTagNameAndUser(String username, String tagname);

    @Query(value = "{'tags.name': ?0 }", count = true)
    public int countArticleByTagName(String tagname);


}
