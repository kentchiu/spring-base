package com.kentchiu.spring.base.domain;


public class IllegalApiUsageException extends RuntimeException {
    public IllegalApiUsageException(String message) {
        super(message);
    }
}
