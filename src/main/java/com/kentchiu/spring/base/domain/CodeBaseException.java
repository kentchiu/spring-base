package com.kentchiu.spring.base.domain;

@Deprecated
public class CodeBaseException extends RuntimeException {

    private String code;
    private String message;
    private Object target;

    public CodeBaseException() {

    }

    public CodeBaseException(String code, String message, Object target) {
        this.code = code;
        this.message = message;
        this.target = target;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
