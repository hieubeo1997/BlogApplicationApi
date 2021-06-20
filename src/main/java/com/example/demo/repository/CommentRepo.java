package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {
    public List<Comment> findCommentByArticleId(String articleId);
    public int countCommentsByArticleId(String articleId);
    public Comment findCommentByArticleIdAndUsername(String articleId, String username);
    
}
