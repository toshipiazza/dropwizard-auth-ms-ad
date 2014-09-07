package com.commercehub.dropwizard.authentication;

import com.commercehub.dropwizard.authentication.groups.LookupGroupResolverStrategy;
import com.commercehub.dropwizard.authentication.groups.NoOpGroupResolverStrategy;
import com.commercehub.dropwizard.authentication.groups.TrivialGroupResolverStrategy;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.*;

import static com.commercehub.dropwizard.authentication.AdConstants.SCHEMA_ATTR_MEMBEROF;
import static com.commercehub.dropwizard.authentication.AdConstants.SCHEMA_ATTR_SAMACCOUNTNAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class AdAuthenticator<T> implements Authenticator<BasicCredentials, T> {

    private static final Logger LOG = LoggerFactory.getLogger(AdAuthenticator.class);
    private AdConfiguration configuration;
    private AdPrincipalMapper<T> mapper;


    public static AdAuthenticator<AdPrincipal> createDefault(AdConfiguration configuration){
        return new AdAuthenticator(configuration, AdPrincipalMapper.DEFAULT);
    }

    public AdAuthenticator(AdConfiguration configuration, AdPrincipalMapper<T> mapper){
        this.configuration = checkNotNull(configuration);
        this.mapper = mapper;
    }

    @Override
    public Optional<T> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return doAuthenticate(AdCredentials.fromBasicCredentials(credentials));
    }

    private Optional<T> doAuthenticate(AdCredentials credentials) throws AuthenticationException {

        DirContext boundContext = bindUser(credentials);
        if(boundContext!=null){
            AdPrincipal principal = getAdPrincipal(boundContext, credentials);
            if(authorized(principal)){
                return Optional.fromNullable(mapper.map(principal));
            }else{
                Set<String> missingGroups = configuration.getRequiredGroups();
                missingGroups.removeAll(principal.getGroupNames());
                LOG.warn(String.format("%s authenticated successfully but did not have authority. Missing Groups: %s", credentials.getUsername(), missingGroups.toString()));
            }
        }
        return Optional.absent();
     }

    private boolean authorized(AdPrincipal principal) {
        boolean authorized = true;
        for(String requiredGroup: configuration.getRequiredGroups()){
            authorized = authorized && principal.getGroupNames().contains(requiredGroup);
        }
        return authorized;
    }


    private AdPrincipal getAdPrincipal(DirContext boundContext, AdCredentials credentials) throws AuthenticationException {
        try {
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchCtls.setReturningAttributes(configuration.getAttributeNames());
            NamingEnumeration<SearchResult> results = boundContext.search(configuration.getDomainBase(), String.format(configuration.getUsernameFilterTemplate(), credentials.getsAMAccountName()), searchCtls);
            SearchResult userResult = results.hasMoreElements() ? results.next() : null;

            if(userResult==null || results.hasMoreElements()){
                throw new AuthenticationException(String.format("Inconsistent search search for %s. Bind succeeded but post bind lookup failed. Assumptions/logic failed?", credentials.getUsername()));
            }

            Map<String, Object> attributes = AdUtilities.simplify(userResult.getAttributes());
            return new AdPrincipal((String)attributes.get(SCHEMA_ATTR_SAMACCOUNTNAME), resolveGroupNames(boundContext, (Set) attributes.get(SCHEMA_ATTR_MEMBEROF)), attributes);
        } catch (NamingException e) {
            throw new AuthenticationException("User search failed. Configuration error?", e);
        }
    }

    private Set<String> resolveGroupNames(DirContext boundContext, Set<String> groupDNs){
        switch(configuration.getGroupResolutionMode()){
            case AdConfiguration.NOOP_GROUP_RESOLV:
                return new NoOpGroupResolverStrategy().resolveGroups(boundContext, groupDNs);
            case AdConfiguration.TRIVIAL_GROUP_RESOLV:
                return new TrivialGroupResolverStrategy().resolveGroups(boundContext, groupDNs);
            case AdConfiguration.LOOKUP_GROUP_RESOLV:
                return new LookupGroupResolverStrategy().resolveGroups(boundContext, groupDNs);
            default:
                throw new RuntimeException("No matching group resolution strategy found");
        }

    }




    private DirContext bindUser(AdCredentials credentials) throws AuthenticationException{
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        //See: http://docs.oracle.com/javase/jndi/tutorial/ldap/connect/create.html#TIMEOUT
        properties.put("com.sun.jndi.ldap.read.timeout", configuration.getReadTimeout()+"");  // How long to wait for a read response
        properties.put("com.sun.jndi.ldap.connect.timeout", configuration.getConnectionTimeout()+"");  // How long to wait for a network connection
        properties.put(Context.PROVIDER_URL, configuration.getLdapUrl());
        properties.put(Context.SECURITY_PRINCIPAL, credentials.getUserPrincipalName(configuration.getDomain()));
        properties.put(Context.SECURITY_CREDENTIALS, credentials.getPassword());
        properties.put(Context.REFERRAL, configuration.getGroupResolutionMode()==AdConfiguration.LOOKUP_GROUP_RESOLV?"follow":"ignore");
        try {
            return new InitialDirContext(properties);
        } catch (javax.naming.AuthenticationException e) {
            LOG.warn(String.format("User: %s failed to authenticate. Bad Credentials", credentials.getUsername()));
            return null;
        } catch (NamingException e) {
            throw new AuthenticationException("Could not bind with AD", e);
        }
    }
}

