package com.kentchiu.spring.base.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class DomainUtil {

    private DomainUtil() {
    }

    public static String toJson(Object object) {
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String json = om.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static void initLazy(Object entity) {
        entity.toString();
    }

    public static void initLazy(Collection entities) {
        entities.size();
    }


    public static void copyNotNullProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            if (value != null) {
                                writeMethod.invoke(target, value);
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    public static void copyProperties(Map<String, Object> source, Object target, String... properties) {
        registerConverters();

        if (properties.length == 0) {
            properties = allPropertiesAsArray(target.getClass());
        }

        for (String property : properties) {
            Object value = source.get(property);
            try {
                if (source.containsKey(property)) {
                    Class<?> type = BeanUtils.findPropertyType(property, target.getClass());
                    if (Date.class.equals(type)) {
                        if (value != null && StringUtils.isNotBlank(value.toString())) {
                            value = DateUtils.parseDate(value.toString(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm");
                        }
                    }
                    if (BigDecimal.class.equals(type)) {
                        if (value == null || StringUtils.isBlank(value.toString())) {
                            value = BigDecimal.ZERO;
                        }
                    }
                    if (!(value instanceof Map)) {
                        org.apache.commons.beanutils.BeanUtils.setProperty(target, property, value);
                    }
                    //logger.trace("set property [{}] to [{}]", params, value);
                }
            } catch (IllegalAccessException | InvocationTargetException | ParseException e) {
                //logger.warn("set property [" + property + "] fail", e);
                e.printStackTrace();
            }
        }
    }

    public static void copyProperties(Object source, Object target, String... properties) {
        registerConverters();
        BeanUtils.copyProperties(source, target, properties);
    }

    private static void registerConverters() {
        Date defaultValue = null;
        DateConverter converter = new DateConverter(defaultValue);
        ConvertUtils.register(converter, Date.class);
    }

    public static String allPropertiesAsString(Class clazz) {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
        return Arrays.asList(pds).stream().filter(pd -> !StringUtils.equals("class", pd.getName())).map(pd -> "\"" + pd.getName() + "\"").collect(Collectors.joining(","));
    }

    public static String[] allPropertiesAsArray(Class<?> clazz) {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
        return Arrays.asList(pds).stream().filter(pd -> !StringUtils.equals("class", pd.getName())).map(pd -> pd.getName()).collect(Collectors.toList()).toArray(new String[pds.length]);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date != null) {
            if (date instanceof java.sql.Date) {
                return ((java.sql.Date) date).toLocalDate().atTime(0, 0, 0);
            }
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            return null;
        }
    }

    public static LocalDate toLocalDate(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            return null;
        }
    }
}
