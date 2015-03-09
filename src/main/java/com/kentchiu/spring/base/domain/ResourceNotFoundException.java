package com.kentchiu.spring.base.domain;


import org.apache.commons.lang3.StringUtils;

public class ResourceNotFoundException extends RuntimeException {

    private Class target;
    private String id;
    private String message;

    public ResourceNotFoundException(Class<?> clazz, String id) {
        this.target = clazz;
        this.message = id;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    @Override
    public String getMessage() {
        if (StringUtils.isNotBlank(message)) {
            return message;
        } else {
            return String.format("resource %s@%d not found", target.getName(), id);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
