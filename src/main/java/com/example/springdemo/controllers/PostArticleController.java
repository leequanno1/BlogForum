package com.example.springdemo.controllers;

import com.example.springdemo.services.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posting")
public class PostArticleController {
    @GetMapping("")
    public String writeNew(Model model, HttpServletRequest request) {
        UserInfoService.addUserInfoToModel(model, request);
        return "write_new_article";
    }
}
