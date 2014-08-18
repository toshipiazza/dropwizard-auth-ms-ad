package com.commercehub.dropwizard.authentication.groups;

import com.commercehub.dropwizard.authentication.AdUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.*;

public class LookupGroupResoloverStrategy implements GroupResoloverStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(LookupGroupResoloverStrategy.class);

    @Override
    public Set<String> resolveGroups(DirContext boundContext, Collection<String> groupDns) {
        Set<String> result = new HashSet();
        StringBuilder query = new StringBuilder("(&(objectClass=group)(|");
        for(String groupDn : groupDns){
            query.append(String.format("(distinguishedName=%s)", groupDn));
        }
        query.append(") )");
        System.out.println(query);
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(new String[]{"sAMAccountName"});

        try {

            NamingEnumeration<SearchResult> searchResults = boundContext.search("dc=nexus,dc=commercehub,dc=com", query.toString(), searchCtls);
            while(searchResults.hasMore()){
                SearchResult group = searchResults.next();
                Map<String, Object> attributes = AdUtilities.simplify(group.getAttributes());
                result.add((String) attributes.get("sAMAccountName"));
            }
        } catch (NamingException e) {
            LOG.error("Failed to lookup groups, returning empty set", e);
            return Collections.emptySet();

        }
        return result;
    }

}
