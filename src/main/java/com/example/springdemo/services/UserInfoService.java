package com.example.springdemo.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

public class UserInfoService {

    public static void addUserInfoToModel(Model model, HttpServletRequest request) {
        Map<String, Object> userInfo = null;
        if(CookieService.getCookieValue(request, CookieService.cookieUserIdKey) != null) {
            userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");
        }
        model.addAttribute("userInfo", userInfo);
    }
}
