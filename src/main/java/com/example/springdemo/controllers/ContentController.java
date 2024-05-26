package com.example.springdemo.controllers;

import com.example.springdemo.services.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/content")
public class ContentController {
    @GetMapping("")
    public String showContent(Model model, HttpServletRequest request) {
        UserInfoService.addUserInfoToModel(model,request);




        return "content";
    }
}
