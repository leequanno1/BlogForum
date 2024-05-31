package com.example.springdemo.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

public class UserInfoService {

    /**
     * This function help get the user information map and add it to model.
     * @param model a distance Model instance.
     * @param request a HttpServletRequest instance.
     * */
    public static void addUserInfoToModel(Model model, HttpServletRequest request) {
        Map<String, Object> userInfo = getUserInfo(request);
        model.addAttribute("userInfo", userInfo);
    }

    /**
     * This function help get the user information map.
     * @param request a HttpServletRequest instance contain cookieUserIdKey.
     * */
    public static Map<String, Object> getUserInfo(HttpServletRequest request) {
        Map<String, Object> userInfo = null;
        if(CookieService.getCookieValue(request, CookieService.cookieUserIdKey) != null) {
            userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");
        }
        return userInfo;
    }
}
