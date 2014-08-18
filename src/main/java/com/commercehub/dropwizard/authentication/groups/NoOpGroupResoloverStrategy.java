package com.commercehub.dropwizard.authentication.groups;

import javax.naming.directory.DirContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NoOpGroupResoloverStrategy implements GroupResoloverStrategy {
    @Override
    public Set<String> resolveGroups(DirContext boundContext, Collection<String> groupDns) {
        return new HashSet<>(groupDns);
    }
}
