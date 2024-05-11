package com.example.springdemo.services;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a Service class provide the status code message body.
 * Those status code body contain 1 key: "message"
 * */
public class StatusBodyMessageService {

    /**
     * This function return the custom message value
     * @param message This is the message value
     * */
    public static Map<String,String > statusMessage(String message) {
        Map<String, String> res = new HashMap<>();
        res.put("message", message);
        return res;
    }

    /**
     * This function is used when the status code is OK
     * */
    public static Map<String,String> statusOk() {
        return statusMessage("OK");
    }

    /**
     * This function is used when the status code is NOT_FOUND
     * */
    public static Map<String,String> statusNotFound() {
        return statusMessage("NOT_FOUND");
    }

    /**
     * This function is used when the status code is INTERNAL_SERVER_ERROR
     * */
    public static Map<String,String> statusInternalServerError() {
        return statusMessage("INTERNAL_SERVER_ERROR");
    }

    /**
     * This function is used when the status code is BAD_REQUEST
     * */
    public static Map<String,String> statusBadRequest() {
        return statusMessage("BAD_REQUEST");
    }

    /**
     * This function is used when the status code is NOT_ACCEPTABLE
     * */
    public static Map<String,String> statusNotAcceptable() {
        return statusMessage("NOT_ACCEPTABLE");
    }
}
