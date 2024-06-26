package com.example.springdemo.models;

public class SignUpForm {

    private String username;

    private String password;

    private String email;

    private String tokenValue;

    public SignUpForm() {
        username = "";
        password = "";
        tokenValue = "";
        email = "";
    }

    public SignUpForm(String username, String password, String email, String tokenValue) {
        this.username = username;
        this.password = password;
        this.tokenValue = tokenValue;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

}
