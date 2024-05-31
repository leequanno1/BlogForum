package com.example.springdemo.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieService {

    public static final String cookieUserIdKey = "SessionUserID";

    /**
     * This function handle get the String value of a cookie form the request.
     * @param request the HttpServletRequest object - the request instance
     * @param cookieName the cookie's key name.
     * @return cookie's value.
     * */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        // Will apply for filter later
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }
}
