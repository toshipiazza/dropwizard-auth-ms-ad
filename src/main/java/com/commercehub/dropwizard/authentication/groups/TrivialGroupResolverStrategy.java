package com.commercehub.dropwizard.authentication.groups;

import javax.naming.directory.DirContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TrivialGroupResolverStrategy implements GroupResolverStrategy {
    @Override
    public Set<String> resolveGroups(DirContext boundContext, Collection<String> groupDns) {
        Set<String> result = new HashSet<>();
        for(String groupDn : groupDns){
            result.add(groupDn.substring(groupDn.indexOf('=') + 1, groupDn.indexOf(',')));
        }
        return result;
    }
}
