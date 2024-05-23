package com.example.springdemo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/newarticle")
public class PostArticleController {
    @PostMapping("/add")
    public String addArticle() {
        return "";
    }
}
