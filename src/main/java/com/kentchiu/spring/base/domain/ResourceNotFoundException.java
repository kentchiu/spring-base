package com.kentchiu.spring.base.domain;


import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class ResourceNotFoundException extends CodeBaseException {

    private Class target;
    private UUID uuid;
    private String message;

    public ResourceNotFoundException(Class<?> clazz, UUID uuid) {
        super();
        this.target = clazz;
        this.uuid = uuid;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
        return String.format("resource %s@%s not found", target.getSimpleName(), uuid);
    }
}
