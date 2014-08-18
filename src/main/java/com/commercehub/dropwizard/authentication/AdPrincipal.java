package com.commercehub.dropwizard.authentication;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.*;

public class AdPrincipal {

    private final String username;
    private final Set groupNames ;
    private Map<String, Object> retrievedAttributes;

    public AdPrincipal(String username, Set groupNames) {
        this(username, groupNames, null);
    }

    public AdPrincipal(String username, Set groupNames, Map retrievedAttributes) {
        this.username = username;
        this.groupNames = groupNames!=null? ImmutableSet.copyOf(groupNames):Collections.EMPTY_SET;
        this.retrievedAttributes = retrievedAttributes!=null?ImmutableMap.copyOf(retrievedAttributes):Collections.EMPTY_MAP;

    }


    public String getUsername() {
        return username;
    }


    public Set getGroupNames() {
        return groupNames;
    }

    public Map<String, Object> getRetrievedAttributes() {
        return ImmutableMap.copyOf(retrievedAttributes);
    }
}
