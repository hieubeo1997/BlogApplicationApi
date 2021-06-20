package com.example.demo.model.request.articleReq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
public class UpdateArticleReq {
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String body;


    public UpdateArticleReq(@NotNull @NotBlank String title, @NotNull @NotBlank String body) {
        this.title = title;
        this.body = body;
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
}
