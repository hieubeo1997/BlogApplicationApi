package com.example.demo.model.mapper;

import com.example.demo.entity.Comment;
import com.example.demo.model.data.CommentData;
import com.example.demo.model.data.ProfileData;
import com.example.demo.model.request.articleReq.CreateArticleReq;
import com.example.demo.model.request.articleReq.CreateCommentReq;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentMapper {
    public static CommentData toCommentData(Comment cmt , ProfileData data) {
        CommentData cmtData = new CommentData();
        cmtData.setId(cmt.getId());
        cmtData.setArticleId(cmt.getArticleId());
        cmtData.setBody(cmt.getBody());
        cmtData.setUser(data);
        cmtData.setCreatedAt(cmt.getCreatedAt());
        cmtData.setUpdatedAt(cmt.getUpdatedAt());
        return cmtData;
    }
    public static Comment toComment(CreateCommentReq req, String articleId, String username){
        Comment cmt = new Comment();
        cmt.setId(UUID.randomUUID().toString());
        cmt.setArticleId(articleId);
        cmt.setBody(req.getBody());
        cmt.setCreatedAt(LocalDateTime.now());
        cmt.setUpdatedAt(LocalDateTime.now());
        cmt.setUsername(username);

        return cmt;
    }
    public static Comment updateComment(CreateCommentReq req, Comment cmt){
        cmt.setBody(req.getBody());
        cmt.setUpdatedAt(LocalDateTime.now());
        return cmt;
    }
}
