package com.example.demo.services;

import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleFavorite;
import com.example.demo.entity.Comment;
import com.example.demo.model.data.ArticleData;
import com.example.demo.model.data.CommentData;
import com.example.demo.model.data.UserData;
import com.example.demo.model.request.articleReq.CreateArticleReq;
import com.example.demo.model.request.articleReq.CreateCommentReq;
import com.example.demo.model.request.articleReq.UpdateArticleReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
public interface ArticleServices {
    public Article createNewArticle(CreateArticleReq req, Principal author);

    public Article updateArticle(UpdateArticleReq req, String username, String slug);

    public Map<String, Object> getArticleByUsername(String targetname, Principal principal, int page, int size);

    public Map<String, Object> feedArticle(String title,Principal principal, int page, int size);

    @Transactional
    public Article deleteArticle(String username, String slug);

    public boolean isUserLikeArticle(String username, String articleId);

    public Article getSingleArticle(String slug);

    public ArticleFavorite likeArticle(String username, String articleId);

    public ArticleFavorite unlikeArticle(String username, String articleId);

    public ArticleData getSingleArticleData(UserData currentUser, String slug);

    public List<CommentData> getCommentsByArticle(String articleId , Principal principal);

    public Comment createComment(CreateCommentReq req, Principal principal, String slug);

    public Comment editComment(CreateCommentReq req, String cmtId, String username);

    public Comment deleteComment(String username, String cmtId);

}
