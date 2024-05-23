package com.example.springdemo.controllers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.controllerservices.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostArticleController {
    @PostMapping("/add")
    public void addArticle(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("tags") String tags,
                             @RequestParam("images") List<MultipartFile> images,
                             HttpServletRequest request) {
        ArticleService articleService = new ArticleService();
        Integer userId = Integer.parseInt(CookieService.getCookieValue(request, CookieService.cookieUserIdKey));
        articleService.handelAddNewArticle(userId, title, content, tags.split(" "), images);
    }
}
