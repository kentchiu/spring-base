package com.kentchiu.spring.base.domain;

public class RestError {

    private int status;
    private String message;
    private String code;
    private Object content;

    public RestError() {
    }

    public RestError(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
