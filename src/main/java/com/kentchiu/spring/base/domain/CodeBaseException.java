package com.kentchiu.spring.base.domain;

import java.util.ArrayList;
import java.util.List;

public class CodeBaseException extends RuntimeException {

    private String message;
    private List<String> contents = new ArrayList<>();

    public CodeBaseException() {
    }

    public CodeBaseException(String message) {
        super(message);
        this.message = message;
    }

    public CodeBaseException(String message, List<String> contents) {
        super(message);
        this.message = message;
        this.contents = contents;
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
