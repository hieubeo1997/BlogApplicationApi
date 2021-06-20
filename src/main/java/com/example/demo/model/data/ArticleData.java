package com.example.demo.model.data;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class ArticleData {
    private String slug;
    private String title;
    private String body;
    private boolean liked;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ArrayList tagList;
    private ProfileData profileAuthor;

    public ArticleData() {
    }

    public ArticleData(String slug, String title, String body, boolean liked, int likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, ArrayList tagList, ProfileData profileAuthor) {
        this.slug = slug;
        this.title = title;
        this.body = body;
        this.liked = liked;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tagList = tagList;
        this.profileAuthor = profileAuthor;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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

    public ArrayList getTagList() {
        return tagList;
    }

    public void setTagList(ArrayList tagList) {
        this.tagList = tagList;
    }

    public ProfileData getProfileAuthor() {
        return profileAuthor;
    }

    public void setProfileAuthor(ProfileData profileAuthor) {
        this.profileAuthor = profileAuthor;
    }
}
