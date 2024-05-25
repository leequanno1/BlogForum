package com.example.springdemo.controllers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping("/")
    public String showIndex(Model model){
        return "index";
    }

}
