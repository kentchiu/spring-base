package com.kentchiu.spring.base.domain;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class CodeBaseException extends RuntimeException {

    private String message;
    private List<String> contents = new ArrayList<>();
    private HttpStatus status;

    public CodeBaseException() {
        this.status = HttpStatus.BAD_REQUEST;
    }

    public CodeBaseException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public CodeBaseException(String message, List<String> contents) {
        super(message);
        this.message = message;
        this.contents = contents;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void addContent(String content) {
        contents.add(content);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }
}
