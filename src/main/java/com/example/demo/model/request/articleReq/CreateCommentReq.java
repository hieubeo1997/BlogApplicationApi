package com.example.demo.model.request.articleReq;

public class CreateCommentReq {
    private String body;

    public CreateCommentReq() {
    }
    public CreateCommentReq(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
