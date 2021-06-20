package com.example.demo.entity;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor

@Document(collection = "article_favorite")
public class ArticleFavorite {
    @Id
    private ObjectId id;

    @Indexed
    @Field(value = "username")
    private String username;

    @Indexed
    @Field(value = "articleId")
    private String articleId;

    public ObjectId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public ArticleFavorite(String username, String articleId) {
        this.username = username;
        this.articleId = articleId;
    }

}
