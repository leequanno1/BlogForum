package com.example.springdemo.controllers;

import com.example.springdemo.services.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String showIndex(Model model){
        return "index";
    }

    @GetMapping("/content")
    public String showContent(Model model) {
        return "content";
    }

    @GetMapping("/personal")
    public String showPersonal (Model model) {
        return "personal";
    }
}
