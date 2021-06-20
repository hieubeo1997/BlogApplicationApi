package com.example.demo.model.mapper;

import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;
import com.example.demo.model.data.ArticleData;
import com.example.demo.model.data.ProfileData;
import com.example.demo.model.data.UserData;
import com.example.demo.model.request.articleReq.CreateArticleReq;
import com.example.demo.model.request.articleReq.UpdateArticleReq;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ArticleMapper {
    public static Article toArticle(CreateArticleReq req , String username){
        Article newpost = new Article();
        newpost.setTitle(req.getTitle());
        newpost.setBody(req.getBody());
        newpost.setTagList(Arrays.stream(req.getTagList()).collect(toSet()).stream().map(Tag::new).collect(toList()));
        newpost.setUpdateAt(LocalDateTime.now());
        newpost.setCreatedAt(LocalDateTime.now());
        newpost.setId(UUID.randomUUID().toString());
        newpost.setSlug(toSlug(req.getTitle()));
        newpost.setUsername(username);
        return newpost;
    }
    public static Article updateArticle(UpdateArticleReq updatedReq, Article param){
        param.setTitle(updatedReq.getTitle());
        param.setBody(updatedReq.getBody());
        param.setSlug(toSlug(updatedReq.getTitle()));
        param.setUpdateAt(LocalDateTime.now());
        return param;
    }

    public static ArticleData toArticleData(Article post , boolean liked, int countLike , ProfileData dataAuthor){
        ArticleData data = new ArticleData();
        data.setSlug(post.getSlug());
        data.setTitle(post.getTitle());
        data.setBody(post.getBody());
        ArrayList tagName = new ArrayList();
        post.getTagList().forEach(
                tag -> {
                    tagName.add(tag.getName());
                }
        );
        data.setTagList(tagName);
        data.setCreatedAt(post.getCreatedAt());
        data.setUpdatedAt(post.getUpdateAt());
        data.setProfileAuthor(dataAuthor);
        data.setLikeCount(countLike);
        data.setLiked(liked);
        return data;
    }
    public static String toSlug(String title){
        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
    }
//    public static String toId(String title){
//        String[] words = title.split(" ");
//        StringBuilder wd = new StringBuilder();
//        for(String w: words){
//            wd.append(w.toUpperCase().charAt(0));
//        }
//        Random r = new Random();
//        String id = wd.toString() + r.nextInt(((100 -1) + 1)+1);
//        return id;
//    }
}
