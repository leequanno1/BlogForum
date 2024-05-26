package com.example.springdemo.form;

public class ForgotPasswordForm {

    private  String email;

    private String tokenValue;

    public ForgotPasswordForm() {
        email = "";
        tokenValue = "";
    }

    public ForgotPasswordForm(String email, String tokenValue) {
        this.email = email;
        this.tokenValue = tokenValue;
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
