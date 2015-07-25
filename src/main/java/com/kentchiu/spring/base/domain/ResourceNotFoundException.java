package com.kentchiu.spring.base.domain;


import org.apache.commons.lang3.StringUtils;

public class ResourceNotFoundException extends CodeBaseException {

    private Class target;
    private String id;
    private String message;

    public ResourceNotFoundException(Class<?> clazz, String id) {
        super();
        this.target = clazz;
        this.id = id;
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
            return defaultMessage();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String defaultMessage() {
        return String.format("resource %s@%s not found", target.getSimpleName(), id);
    }
}
