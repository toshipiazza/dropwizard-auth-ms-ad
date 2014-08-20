package com.commercehub.dropwizard.authentication.groups;

import javax.naming.directory.DirContext;
import java.util.Collection;
import java.util.Set;

public interface GroupResolverStrategy {

    public Set<String> resolveGroups(DirContext boundContext, Collection<String> groupDns);

}
