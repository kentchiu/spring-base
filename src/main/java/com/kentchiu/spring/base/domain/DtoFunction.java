package com.kentchiu.spring.base.domain;


import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DtoFunction<T> implements Function<T, Map<String, Object>> {
    private Map<String, DtoFunction> dtoMap = Maps.newHashMap();
    private String[] properties;

    public DtoFunction() {
    }

    public DtoFunction(String... properties) {
        this.properties = properties;
    }

    @Nullable
    @Override
    public Map<String, Object> apply(@Nullable T input) {
        HashMap<String, Object> map = Maps.newLinkedHashMap();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(input.getClass());

        if (properties == null || properties.length == 0) {
            properties = DomainUtil.allPropertiesAsArray(input.getClass());
        }

        for (String property : properties) {
            Optional<PropertyDescriptor> optionalPd = findPropertyDescriptor(pds, property);
            if (optionalPd.isPresent()) {
                PropertyDescriptor pd = optionalPd.get();
                map.putAll(propertyToMap(input, property, pd));
            } else {
                // FIXME not idea why this err will be dump
                System.err.println("property " + property + " not exist in " + input.getClass());
            }
        }
        return map;
    }

    private Map<String, Object> propertyToMap(T target, String property, PropertyDescriptor pd) {
        Map<String, Object> result = Maps.newLinkedHashMap();
        try {
            Method readMethod = pd.getReadMethod();
            if (readMethod != null) {
                Object value = readMethod.invoke(target);
                result.putAll(propertyToMap(property, value));
            } else {
                System.err.println(target.getClass() + " don't has a getter for property [" + property + "]");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("invoke property " + target.getClass().getName() + "." + property + " fail");
            result.put(property, null);
        }
        return result;
    }

    private Map<String, Object> propertyToMap(String property, Object value) {
        Map<String, Object> result = Maps.newLinkedHashMap();
        if (dtoMap.containsKey(property)) {
            DtoFunction dto = dtoMap.get(property);
            if (value != null) {
                result.put(property, dto.apply(value));
            } else {
                result.put(property, null);
            }
        } else {
            result.put(property, value);
        }
        return result;
    }

    private Optional<PropertyDescriptor> findPropertyDescriptor(PropertyDescriptor[] pds, final String property) {
        return Iterables.tryFind(Arrays.asList(pds), new Predicate<PropertyDescriptor>() {
            @Override
            public boolean apply(@Nullable PropertyDescriptor input) {
                return StringUtils.equals(property, input.getName());
            }
        });
    }

    public void registryNestDto(String property, DtoFunction function) {
        dtoMap.put(property, function);
    }


}