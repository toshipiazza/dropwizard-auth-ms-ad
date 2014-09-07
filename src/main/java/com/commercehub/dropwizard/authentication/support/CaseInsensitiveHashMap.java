package com.commercehub.dropwizard.authentication.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple extension to {@link java.util.HashMap} which does not enforce case sensitivity
 */
public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {

    public CaseInsensitiveHashMap(){
        super();
    }

    public CaseInsensitiveHashMap(Map<String, V> m) {
        super();
        for(Entry<String, V> entry: m.entrySet()){
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V put(String key, V value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public V get(Object key) {
        return super.get(key.toString().toLowerCase());
    }
}
