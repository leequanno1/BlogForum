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

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try{
            UserAccountService service = new UserAccountService();
            if(service.getUser(username, password)){
                return "hello";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index1";
    }

}
