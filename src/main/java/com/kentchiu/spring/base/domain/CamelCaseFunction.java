package com.kentchiu.spring.base.domain;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

/**
 * convert under line  string to lower camel
 * ex: foo_bar -> fooBar
 */
@Deprecated
public class CamelCaseFunction implements Function<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(Map<String, Object> input) {
        Set<Entry<String, Object>> entrySet = input.entrySet();
        Map<String, Object> map = Maps.newHashMap();
        for (Entry<String, Object> each : entrySet) {
            String newKey = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, each.getKey());
            map.put(newKey, each.getValue());
        }
        return map;
    }
}