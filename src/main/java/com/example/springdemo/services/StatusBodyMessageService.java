package com.example.springdemo.services;

import java.util.HashMap;
import java.util.Map;

public class StatusBodyMessageService {
    public static Map<String,String > statusMessage(String message) {
        Map<String, String> res = new HashMap<>();
        res.put("message", message);
        return res;
    }

    public static Map<String,String> statusOk() {
        return statusMessage("OK");
    }

    public static Map<String,String> statusNotFound() {
        return statusMessage("NOT_FOUND");
    }

    public static Map<String,String> statusInternalServerError() {
        return statusMessage("INTERNAL_SERVER_ERROR");
    }

    public static Map<String,String> statusBadRequest() {
        return statusMessage("BAD_REQUEST");
    }

    public static Map<String,String> statusNotAcceptable() {
        return statusMessage("NOT_ACCEPTABLE");
    }
}
