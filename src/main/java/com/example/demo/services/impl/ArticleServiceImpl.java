package com.example.demo.services.impl;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.model.data.*;
import com.example.demo.model.mapper.ArticleMapper;
import com.example.demo.model.mapper.CommentMapper;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.articleReq.CreateArticleReq;
import com.example.demo.model.request.articleReq.CreateCommentReq;
import com.example.demo.model.request.articleReq.UpdateArticleReq;
import com.example.demo.repository.*;
import com.example.demo.services.ArticleServices;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.*;

@Component
public class ArticleServiceImpl implements ArticleServices {
    private ArticleRepository articleRepository;
    private ArticleFavoritedRepo articleFavoritedRepo;
    private CommentRepo commentRepo;
    private FollowRepository followRepository;
    private UserService userService;
    private TagRepository tagRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleFavoritedRepo articleFavoritedRepo, CommentRepo commentRepo, FollowRepository followRepository, UserService userService, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.articleFavoritedRepo = articleFavoritedRepo;
        this.commentRepo = commentRepo;
        this.followRepository = followRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }

    @Override
    public Article createNewArticle(CreateArticleReq req, Principal author) {
        Article post = ArticleMapper.toArticle(req, author.getName());
        if(articleRepository.findArticlesBySlug(post.getSlug()) != null ){
            throw new DuplicateRecordException("This title has been existed!");
        }
        try{
            articleRepository.save(post);
            for(Tag tag : post.getTagList()){
                if(tagRepository.findTagByName(tag.getName()) != null);
                else
                    tagRepository.save(tag);
            }
            return post;
        }catch (InternalServerException e){
            throw new InternalServerException("Database error :  " + e);
        }
    }

    @Override
    public Article updateArticle(UpdateArticleReq req, String username, String slug) {
        Article postEdit = articleRepository.findArticlesBySlug(slug);
        if(postEdit == null) {
            throw new NotFoundException("Your article is not found!");
        }else if(username.equals(postEdit.getUsername()) == false){
            throw  new UnAuthorizeException("You has not authorize to edit this article");
        }
        ArticleMapper.updateArticle(req,postEdit);
        try{
            articleRepository.save(postEdit);
            return postEdit;
        }catch (InternalServerException e){
            throw new InternalServerException("Database error :  " + e);
        }
    }



    @Override
    public Article deleteArticle(String username, String slug) {
        Article deletePost = articleRepository.findArticlesBySlug(slug);
        if(username.equals(deletePost.getUsername()) == false){
            throw new UnAuthorizeException("You cannot delete this post");
        }else if(deletePost == null){
            throw new NotFoundException("Your artile with this title doesnt existed anymore.");
        }
        try{
            List<Comment> comments = commentRepo.findCommentByArticleId(deletePost.getId());
            for(Comment cmt : comments){
                commentRepo.delete(cmt);
            }
            articleRepository.delete(deletePost);
            return deletePost;
        }catch (InternalServerException e){
            throw new InternalServerException("Database error :  " + e);
        }
    }

    @Override
    public boolean isUserLikeArticle(String username, String articleId) {
        ArticleFavorite record = articleFavoritedRepo.findArticleFavoriteByUsernameAndArticleId(username,articleId);
        if(record == null) {
            return false;
        }
        else
            return true;
    }

    @Override
    public ArticleFavorite likeArticle(String username, String articleId) {
        ArticleFavorite checked = articleFavoritedRepo.findArticleFavoriteByUsernameAndArticleId(username,articleId);
        if(checked != null){
            throw new DuplicateRecordException("Errorr");
        }
        else if(!articleRepository.existsById(articleId)){
            throw new BadRequestException("The article doesn't existed");
        }
        ArticleFavorite newRecord = new ArticleFavorite(username, articleId);
        try{
            articleFavoritedRepo.save(newRecord);
        }catch (InternalServerException e){
            throw new InternalServerException("Database error :  " + e);
        }
        return newRecord;
    }

    @Override
    public ArticleFavorite unlikeArticle(String username, String articleId) {
        ArticleFavorite recordSapXoa = articleFavoritedRepo.findArticleFavoriteByUsernameAndArticleId(username,articleId);
        if(recordSapXoa == null){
            throw new NotFoundException("Errorr");
        }
        try{
            articleFavoritedRepo.delete(recordSapXoa);
        }catch (InternalServerException e){
            throw new InternalServerException("Database error :  " + e);
        }
        return recordSapXoa;
    }

    @Override
    public Article getSingleArticle(String slug) {
        if(articleRepository.findArticlesBySlug(slug) == null){
            throw new NotFoundException("Your Article has not existed!");
        }
        return articleRepository.findArticlesBySlug(slug);
    }

    @Override
    public ArticleData getSingleArticleData(UserData currentUser, String slug) {
        Article singlePost = articleRepository.findArticlesBySlug(slug);
        if(singlePost == null) {
            throw new NotFoundException("Not Found");
        }
        UserData nguoivietbai = userService.getUserByUsername(singlePost.getUsername());

        boolean follow = userService.isUserFollowTarget(currentUser.getUsername(), nguoivietbai.getUsername());

        int numberFollower = followRepository.countFollowByTargetname(nguoivietbai.getUsername());

        boolean liked = isUserLikeArticle(currentUser.getUsername(),singlePost.getId());

        ProfileData profileTacgia = UserMapper.toProfile(nguoivietbai,follow,numberFollower);

        int countLiked = articleFavoritedRepo.countArticleFavoriteByArticleId(singlePost.getId());

        return ArticleMapper.toArticleData(singlePost, liked,countLiked,profileTacgia);
    }

    @Override
    public Map<String, Object> getArticleByUsername(String targetname, Principal principal, int page, int size) {
        Map<String, Object> mapResult = new LinkedHashMap<>();
        Page<Article> rawData = articleRepository.findAllByUsername(targetname, PageRequest.of(page, size));
        List<ArticleData> rs = convertToArticleData(principal,rawData.getContent());
        mapResult.put("listData", rs);
        mapResult.put("currentPage", rawData.getNumber());
        mapResult.put("totalRecord", rawData.getTotalElements());
        mapResult.put("totalPages", rawData.getTotalPages());
        mapResult.put("isFirst",rawData.isFirst());
        mapResult.put("hasNext",rawData.hasNext());

        return mapResult;
    }
    @Override
    public Map<String, Object> feedArticle(String title, Principal principal, int page, int size) {
        Map<String, Object> mapResult2 = new LinkedHashMap<>();
        Page<Article> rawData2;
        if(title == null){
            rawData2 = articleRepository.findAll(PageRequest.of(page, size , Sort.by("createdAt").descending()));
        }
        else
            rawData2 = articleRepository.findArticlesByTitleContaining(title, PageRequest.of(page, size));

        List<ArticleData> rs2 = convertToArticleData(principal,rawData2.getContent());
        mapResult2.put("listData", rs2);
        mapResult2.put("currentPage", rawData2.getNumber());
        mapResult2.put("totalRecord", rawData2.getTotalElements());
        mapResult2.put("totalPages", rawData2.getTotalPages());
        mapResult2.put("isFirst",rawData2.isFirst());
        mapResult2.put("hasNext",rawData2.hasNext());

        return mapResult2;
    }

    //Đây là hàm bổ trợ
    public List<ArticleData> convertToArticleData(Principal principal ,List<Article> result){
        List<ArticleData> rs = new ArrayList<>();
        result.forEach(article -> {
            UserData tacgia = userService.getUserByUsername(article.getUsername());
            boolean following = userService.isUserFollowTarget(principal.getName(), tacgia.getUsername());
            int numFollower = followRepository.countFollowByTargetname(tacgia.getUsername());
            int countLike = articleFavoritedRepo.countArticleFavoriteByArticleId(article.getId());
            boolean liked = isUserLikeArticle(principal.getName(), article.getId());
            rs.add(ArticleMapper.toArticleData(article, liked, countLike, UserMapper.toProfile(tacgia,following,numFollower)));
        });
        return rs;
    }

    @Override
    public List<CommentData> getCommentsByArticle(String articleId , Principal principal) {
        List<Comment> listComments = commentRepo.findCommentByArticleId(articleId);
        List<CommentData> commentData = new ArrayList<>();
        listComments.forEach(comment -> {
            UserData tacgia = userService.getUserByUsername(comment.getUsername());
            boolean following = userService.isUserFollowTarget(principal.getName(), tacgia.getUsername());
            int numberFollowerAuthor = followRepository.countFollowByTargetname(tacgia.getUsername());
            commentData.add(CommentMapper.toCommentData(comment, UserMapper.toProfile(tacgia,following,numberFollowerAuthor)));
        });
        return commentData;
    }

    @Override
    public Comment createComment(CreateCommentReq req, Principal principal, String slug) {
        Article post = articleRepository.findArticlesBySlug(slug);
        if(post == null){
            throw new NotFoundException("The Article has not been existed anymore.");
        }else if(principal == null){
            throw new UnAuthorizeException("You have to log in to comment");
        }else {
            try {
                Comment newComment = CommentMapper.toComment(req,post.getId(),principal.getName());
                commentRepo.save(newComment);
                return newComment;
            }catch (Exception e){
                throw new InternalServerException("Internal Error: " + e);
            }
        }
    }

    @Override
    public Comment editComment(CreateCommentReq req, String cmtId , String username) {
        Optional<Comment> cmt = commentRepo.findById(cmtId);
        if(cmt.isEmpty()){
            throw new NotFoundException("Your comment doesn't existed");
        }else if(!username.equals(cmt.get().getUsername())){
            throw new UnAuthorizeException("You cannot edit others comment.");
        }else {
            CommentMapper.updateComment(req, cmt.get());
            try {
                commentRepo.save(cmt.get());
            }catch (Exception e){
                throw new InternalServerException("Error: " + e);
            }
        }
        return  cmt.get();
    }

    @Override
    public Comment deleteComment(String username, String cmtId) {
        Optional<Comment> cmt = commentRepo.findById(cmtId);
        Article post = articleRepository.findArticlesById(cmt.get().getArticleId());
        if(!cmt.isPresent()){
            throw new NotFoundException("Your comment doesn't existed");
        }else if(post == null){
            throw new NotFoundException("Not Found");
        }
        //Người chủ bài viết, hoặc chính người comment mới có thể xóa.
        else if(username.equals(cmt.get().getUsername()) || username.equals(post.getUsername())){
            try {
                commentRepo.delete(cmt.get());
                return cmt.get();
            }catch (Exception e){
                throw new InternalServerException("Error: " + e);
            }
        }else{
            throw new UnAuthorizeException("You cannot delete others comment.");
        }
    }
}
