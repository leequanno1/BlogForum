package com.example.springdemo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderUtil {

    public static String encode(String content) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(content);
    }

    public static boolean matches(String rawContent, String encodedContent) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawContent, encodedContent);
    }

}
