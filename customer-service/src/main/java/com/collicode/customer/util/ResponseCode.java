package com.collicode.customer.util;


public enum ResponseCode {
    NOTFOUND("404", "Not Found"),
    INTERNALSERVERERROR("500", "Generic error occurred");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

