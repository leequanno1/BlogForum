package com.example.springdemo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posting")
public class PostArticleController {
    @GetMapping("")
    public String writeNew(Model model) {
        return "write_new_article";
    }
}
