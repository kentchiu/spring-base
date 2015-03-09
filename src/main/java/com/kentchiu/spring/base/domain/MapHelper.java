package com.kentchiu.spring.base.domain;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapHelper<K, V> implements Map<K, V> {


    protected ConfigurableConversionService conversionService = new DefaultConversionService();
    private Map<K, V> delegate;

    public MapHelper(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    public String get(String key, String defaultValue) {
        if (delegate.containsKey(key)) {
            if (get(key) == null) {
                return defaultValue;
            } else {
                return get(key).toString();
            }
        }
        return defaultValue;
    }

    <T> T get(String key, Class<T> targetType) {
        return conversionService.convert(get(key), targetType);
    }

    public <T> T get(String key, Class<T> targetType, T defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return conversionService.convert(get(key), targetType);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public V put(K key, V value) {
        return delegate.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }
}
