package com.example.springdemo.models;

public class ResetPasswordForm {

    private String password;

    private String rePassword;

    public ResetPasswordForm() {
        password = "";
        rePassword = "";
    }

    public ResetPasswordForm(String password, String rePassword) {
        this.password = password;
        this.rePassword = rePassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }


}
