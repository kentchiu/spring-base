package com.kentchiu.spring.base.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DomainUtil {

    private static Logger logger = LoggerFactory.getLogger(DomainUtil.class);

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


    public static void copyNotNullProperties(Object source, Object target, String... ignoreProperties) {

        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(source.getClass());

        List<String> nullValueProperties = Arrays.stream(pds).filter(pd -> {
            try {
                Object invoke = pd.getReadMethod().invoke(source);
                return invoke == null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return true;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return true;
            }
        }).map(pd -> pd.getName()).collect(Collectors.toList());
        logger.debug("ignore property: {}", nullValueProperties);
        BeanUtils.copyProperties(source, target, Iterables.toArray(nullValueProperties, String.class));
        copyDateProperties(source, target);
    }

    private static void copyDateProperties(Object source, Object target) {
        // 處理 target 的 date properties
        List<String> dateProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(target.getClass()))
                .filter(pd -> pd.getPropertyType().isAssignableFrom(Date.class))
                .map(pd -> pd.getName()).collect(Collectors.toList());

        for (String p : dateProperties) {
            try {
                // 取得source中對應名稱的屬性
                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(source.getClass(), p);
                if (pd != null) {
                    Date srcDateValue = getDateValueOfSource(source, target, p);
                    PropertyDescriptor pd2 = BeanUtils.getPropertyDescriptor(target.getClass(), p);
                    pd2.getWriteMethod().invoke(target, srcDateValue);
                    logger.trace("set date property {} to {}", p, srcDateValue);
                }
            } catch (Exception e) {
                logger.warn("set date property {} fail", e);
            }
        }
    }

    private static Date getDateValueOfSource(Object source, Object target, String p) throws IllegalAccessException, InvocationTargetException {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(source.getClass(), p);
        if (pd == null) {
            return null;
        }
        Object srcValue = pd.getReadMethod().invoke(source);

        if (pd.getReadMethod().getReturnType().equals(String.class)) {
            String strValue = (String) srcValue;
            if (strValue == null) {
                return (Date) pd.getReadMethod().invoke(target);
            } else if (StringUtils.isBlank(strValue)) {
                return null;
            } else {
                try {
                    return DateUtils.parseDate(srcValue.toString(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm");
                } catch (ParseException e) {
                    return null;
                }
            }
        } else if (pd.getReadMethod().getReturnType().isAssignableFrom(Date.class)) {
            Date dateValue = (Date) srcValue;
            return dateValue != null ? dateValue : (Date) pd.getReadMethod().invoke(target);
        } else {
            return null;
        }
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
