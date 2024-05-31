package com.example.springdemo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderUtil {

    /**
     * Encodes the given content using BCrypt.
     *
     * @param content the raw content to be encoded
     * @return the encoded content
     */
    public static String encode(String content) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(content);
    }

    /**
     * Checks if the raw content matches the encoded content.
     *
     * @param rawContent the raw content to be checked
     * @param encodedContent the encoded content to be matched against
     * @return true if the raw content matches the encoded content, false otherwise
     */
    public static boolean matches(String rawContent, String encodedContent) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawContent, encodedContent);
    }

}
