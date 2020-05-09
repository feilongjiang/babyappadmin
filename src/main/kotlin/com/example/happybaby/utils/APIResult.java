package com.example.happybaby.utils;


import com.example.happybaby.exception.BaseHttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

public class APIResult {

    private Integer code;
    private String message;
    private Object data;

    private APIResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static APIResult ok(Object data) {
        return create(BaseHttpStatus.OK, data);
    }

    public static APIResult ok(String message) {
        return create(BaseHttpStatus.OK, message);
    }

    public static APIResult ok() {
        return create(BaseHttpStatus.OK);
    }

    public static APIResult ng(String message) {
        return create(BaseHttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static APIResult ng() {
        return create(BaseHttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static APIResult create(BaseHttpStatus status) {
        return create(status, status.getMessage(), null);
    }

    public static APIResult create(BaseHttpStatus status, String message) {
        return create(status, message, null);
    }

    public static APIResult create(BaseHttpStatus status, Object object) {
        return new APIResult(status.getCode(), status.getMessage(), object);
    }

    public static APIResult create(BaseHttpStatus status, String message, Object object) {
        return new APIResult(status.getCode(), message, object);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
