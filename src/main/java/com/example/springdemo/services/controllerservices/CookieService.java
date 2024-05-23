package com.example.springdemo.services.controllerservices;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieService {

    public static final String cookieUserIdKey = "serverUserId";
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        // Will apply for filter later
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                value = cookie.getValue();
                break;
            }
        }
        return value;
    }
}
