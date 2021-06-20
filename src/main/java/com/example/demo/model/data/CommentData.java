package com.example.demo.model.data;

import java.time.LocalDateTime;

public class CommentData {
    private String id;
    private String articleId;
    private String body;
    private ProfileData user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentData() {}

    public CommentData(String id, String articleId, String body, ProfileData user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.articleId = articleId;
        this.body = body;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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

    public ProfileData getUser() {
        return user;
    }

    public void setUser(ProfileData user) {
        this.user = user;
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
