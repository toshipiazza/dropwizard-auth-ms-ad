package com.commercehub.dropwizard.authentication.support;

import java.util.HashMap;

/**
 * Created by ghogan on 8/16/14.
 */
public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {

    @Override
    public V put(String key, V value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public V get(Object key) {
        return super.get(key.toString().toLowerCase());
    }
}
