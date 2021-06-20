package com.example.demo.model.request.articleReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateArticleReq {
    @NotBlank
    @NotNull
    private String title;

    @NotNull
    @NotBlank
    private String body;

    private String[] tagList;

    public CreateArticleReq(@NotBlank @NotNull String title, @NotNull @NotBlank String body, String[] tagList) {
        this.title = title;
        this.body = body;
        this.tagList = tagList;
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

    public String[] getTagList() {
        return tagList;
    }

    public void setTagList(String[] tagList) {
        this.tagList = tagList;
    }
}
