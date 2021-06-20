package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field(value = "body")
    private String body;

    @Indexed
    @Field(value = "articleId")
    private String articleId;

    @Indexed
    @Field(value = "username")
    private String username;

    @Field(value = "createdAt")
    private LocalDateTime createdAt;

    @Field(value = "updateAt")
    private LocalDateTime updatedAt;

    public Comment() {
    }

    public Comment(String id, String body, String articleId, String username, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.body = body;
        this.articleId = articleId;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
