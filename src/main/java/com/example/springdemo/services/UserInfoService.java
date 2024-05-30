package com.example.springdemo.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

public class UserInfoService {

    public static void addUserInfoToModel(Model model, HttpServletRequest request) {
        Map<String, Object> userInfo = getUserInfo(request);
        model.addAttribute("userInfo", userInfo);
    }

    public static Map<String, Object> getUserInfo(HttpServletRequest request) {
        Map<String, Object> userInfo = null;
        if(CookieService.getCookieValue(request, CookieService.cookieUserIdKey) != null) {
            userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");
        }
        return userInfo;
    }
}
