package com.example.springdemo.services.controllerservices;

import com.example.springdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

public class UserInfoService {

    /**
     * This function will be call every a request to /personal/*
     * This function will be applied in the Personal filter later.
     * If no "userId" cookie was found then return value of false.
     * Otherwise, return value of true.
     * @param request the request information include: server cookies.
     * @param session the session
     * */
    public static Map<String,Object> handleSetUserInfo(HttpServletRequest request, HttpSession session) {
        try {
            String userId = CookieService.getCookieValue(request, "serverUserId");
            if(session.getAttribute(userId) == null) {
                UserService userService = new UserService();
                session.setAttribute(userId, userService.getAccountInfoByUserId(Integer.parseInt(userId)));
            }
            return (Map<String,Object>) session.getAttribute(userId);
        } catch (Exception e) {
            return null;
        }
    }

    public static void addUserInfoToModel(Model model, HttpServletRequest request, HttpSession session) {
        Map<String, Object> userInfo = handleSetUserInfo(request, session);
        model.addAttribute("userInfo", userInfo);
    }
}
