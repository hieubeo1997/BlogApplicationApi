package com.example.demo.controller;
import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleFavorite;
import com.example.demo.entity.Comment;
import com.example.demo.model.data.ArticleData;
import com.example.demo.model.data.CommentData;
import com.example.demo.model.data.UserData;
import com.example.demo.model.request.articleReq.CreateArticleReq;
import com.example.demo.model.request.articleReq.CreateCommentReq;
import com.example.demo.model.request.articleReq.UpdateArticleReq;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.services.ArticleServices;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/article")
public class ArticleController {
    private ArticleServices articleServices;
    private UserService userService;
    private ArticleRepository articleRepository;
    @Autowired
    public ArticleController(ArticleServices articleServices, UserService userService, ArticleRepository articleRepository) {
        this.articleServices = articleServices;
        this.userService = userService;
        this.articleRepository = articleRepository;
    }
    // Controller của article
    //----------------API cho non-user----------------
    public Map<String,Object> toResponseApi(List<Article> list,
                                            int currentPage,
                                            int total,
                                            int pages,
                                            boolean isFirst,
                                            boolean hasNext)
    {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("listArticle",list);
        response.put("currentPage", currentPage);
        response.put("totalRecords", total);
        response.put("totalPages", pages);
        response.put("isFirst", isFirst);
        response.put("hasNext", hasNext);
        return response;
    }
    @GetMapping(value = "/non-user/list")
    public ResponseEntity<?> getArticles(@RequestParam(required = false, defaultValue = "3", value = "size") int size,
                                         @RequestParam(required = false, value = "title") String title,
                                         @RequestParam(required = false, value = "author") String author,
                                         @RequestParam(value = "page",required = false, defaultValue = "0") int page){
        Page<Article> listData;
        if(title == null && author == null) {
           listData = articleRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));

        }else if(title != null && author == null){
           listData =  articleRepository.findArticlesByTitleContaining(title,PageRequest.of(page,size));
        }else if(title == null && author !=null){
            listData = articleRepository.findAllByUsername(author, PageRequest.of(page,size));
        }else{
            listData = articleRepository.findArticlesByUsernameAndTitleContaining(author,title,PageRequest.of(page, size));
        }
        if(listData.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<Article> listArticle = listData.getContent();
        Map<String, Object> response = toResponseApi(listArticle,
                                                    listData.getNumber(),
                                                    listData.getNumberOfElements(),
                                                    listData.getTotalPages(),
                                                    listData.isFirst(),
                                                    listData.hasNext());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //OK
    @GetMapping(value = "/non-user/{slug}")
    public ResponseEntity<?> getSingleArticleNonUser(@PathVariable(value = "slug") String slug){
        Article record = articleServices.getSingleArticle(slug);
        return ResponseEntity.ok(record);
    }


    //------------api cho User-------------
    //Them Article rat OK
    @PostMapping(value = "/add")
    public ResponseEntity<?> createArticle(@Valid @RequestBody CreateArticleReq req , Principal principal){
        Article newArticle = articleServices.createNewArticle(req,principal);
        return ResponseEntity.ok(newArticle);
    }
    //Like Ok
    @PostMapping(value = "/{slug}/like")
    public ResponseEntity<?> likeArticle(Principal principal, @PathVariable(value = "slug") String slug){
        ArticleFavorite data = articleServices.likeArticle(principal.getName(), articleServices.getSingleArticle(slug).getId());
        return ResponseEntity.ok(data);
    }
    //Ok
    @GetMapping(value = "/{slug}")
    public ResponseEntity<?> getSingleArticle(@PathVariable(value = "slug") String slug , Principal principal){
        UserData currentUser = userService.getUserByUsername(principal.getName());
        ArticleData data = articleServices.getSingleArticleData(currentUser, slug);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getArticlesforUsers(@RequestParam(required = false, defaultValue = "3", value = "size") int size,
                                                 @RequestParam(required = false, value = "author") String author,
                                                 @RequestParam(required = false, value = "title") String title,
                                                 @RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                                 Principal principal){
        Map<String, Object> rs;
        if(author == null){
            rs = articleServices.feedArticle(title,principal,page,size);
        }else
            rs = articleServices.getArticleByUsername(author,principal,page,size);
        return ResponseEntity.ok(rs);
    }

    //OK
    @PutMapping(value = "/{slug}/edit")
    public ResponseEntity<?> editArticle(@PathVariable(value = "slug") String slug, @RequestBody UpdateArticleReq req, Principal principal){
        Article editPost = articleServices.updateArticle(req,principal.getName(),slug);
        return ResponseEntity.ok(editPost);
    }
    //Da OK
    @DeleteMapping(value = "/{slug}/delete")
    public ResponseEntity<?> deleteArticle(@PathVariable(value = "slug") String slug, Principal principal){
        Article deleted = articleServices.deleteArticle(principal.getName(),slug);
        return ResponseEntity.ok(deleted);
    }
    //Unlike OK
    @DeleteMapping(value = "/{slug}/unlike")
    public ResponseEntity<?> unlikeArticle (@PathVariable(value = "slug") String slug, Principal principal){
        ArticleFavorite data = articleServices.unlikeArticle(principal.getName(), articleServices.getSingleArticle(slug).getId());
        return ResponseEntity.ok(data);
    }

    //Phần comment - controller
    //Da OK
    @PostMapping(value = "/{slug}/comment/add")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentReq req, Principal principal , @PathVariable(value = "slug") String slug){
        Comment singleComment = articleServices.createComment(req, principal, slug);
        return ResponseEntity.ok(singleComment);
    }
    //Da OK
    @GetMapping(value = "/{slug}/comment/get")
    public ResponseEntity<?> getCommentInArticle(@PathVariable(value = "slug") String slug, Principal principal){
        Article targetPost = articleServices.getSingleArticle(slug);
        List<CommentData> commentList = articleServices.getCommentsByArticle(targetPost.getId(), principal);
        return ResponseEntity.ok(commentList);
    }
    //Da OK
    @PutMapping(value = "/{slug}/comment/edit")
    public ResponseEntity<?> editComment(@PathVariable(value = "slug") String slug,
                                         @RequestParam(value = "id") String id,
                                         @RequestBody CreateCommentReq req,
                                         Principal principal){
        String username = principal.getName();
        Comment cmtEdit = articleServices.editComment(req, id, username);
        return ResponseEntity.ok(cmtEdit);
    }
    //Đã Ok rồi
    @DeleteMapping(value = "/{slug}/comment/delete")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "slug") String slug,
                                           @RequestParam(value = "id") String id,
                                           Principal principal){
        String username = principal.getName();
        Comment deleteComment = articleServices.deleteComment(username, id);
        return ResponseEntity.ok(deleteComment);
    }
}
