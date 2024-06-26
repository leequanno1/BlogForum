package com.example.springdemo.models;

import java.util.List;

public class ChangeInfoForm {
    private String lastAvtURL;
    private String base64Output;
    private String displayName;
    private String userDescription;

    public ChangeInfoForm(String lastAvtURL, String base64Output, String displayName, String userDescription) {
        this.lastAvtURL = lastAvtURL;
        this.base64Output = base64Output;
        this.displayName = displayName;
        this.userDescription = userDescription;
    }

    public ChangeInfoForm() {
        this.lastAvtURL = "";
        this.base64Output = "";
        this.displayName = "";
        this.userDescription = "";
    }

    public String getLastAvtURL() {
        return lastAvtURL;
    }

    public void setLastAvtURL(String lastAvtURL) {
        this.lastAvtURL = lastAvtURL;
    }

    public String getBase64Output() {
        return base64Output;
    }

    public void setBase64Output(String base64Output) {
        this.base64Output = base64Output;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
