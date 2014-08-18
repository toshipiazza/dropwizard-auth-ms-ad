package com.commercehub.dropwizard.authentication;

import com.google.common.collect.Sets;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class AdConfiguration {

    //Simply use the CN of the group as the role name.
    public static final int TRIVIAL_GROUP_RESOLV=1;

    //Resolve the group dn to the sAMAccount name for the group
    public static final int LOOKUP_GROUP_RESOLV=2;

    //Do nothing, Simply return the full DN returned as the memberOf attribute
    public static final int NOOP_GROUP_RESOLV=0;

    @NotNull
    @Valid
    private String domain;
    private String domainController;

    @NotNull
    @Valid
    private String usernameFilterTemplate = "(&((&(objectCategory=Person)(objectClass=User)))(sAMAccountName=${username}))";
    private int groupResolutionMode = TRIVIAL_GROUP_RESOLV;
    private String[] attributeNames = new String[]{"sAMAccountName", "mail", "memberOf"};
    private boolean sslEnabled = true;

    @NotNull
    @Valid
    private Set<String> requiredGroups = Sets.newHashSet();

    public String getDomainController() {
        return domainController!=null ? domainController : domain ;
    }

    public void setDomainController(String domainController) {
        this.domainController = domainController;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsernameFilterTemplate() {
        return usernameFilterTemplate;
    }

    public void setUsernameFilterTemplate(String usernameFilterTemplate) {
        this.usernameFilterTemplate = usernameFilterTemplate;
    }

    public int getGroupResolutionMode() {
        return groupResolutionMode;
    }

    public void setGroupResolutionMode(int groupResolutionMode) {
        this.groupResolutionMode = groupResolutionMode;
    }

    public String getDomainBase() {
        char[] namePair = domain.toUpperCase().toCharArray();
        String dn = "DC=";
        for (int i = 0; i < namePair.length; i++) {
            if (namePair[i] == '.') {
                dn += ",DC=" + namePair[++i];
            } else {
                dn += namePair[i];
            }
        }
        return dn;
    }

    public Set<String> getRequiredGroups() {
        return Sets.newCopyOnWriteArraySet(requiredGroups);
    }

    public void setRequiredGroups(Set<String> requiredGroups) {
        this.requiredGroups = requiredGroups;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public void setAttributeNames(String[] attributeNames) {
        this.attributeNames = attributeNames;
    }

    public String[] getAttributeNames() {
        return attributeNames;
    }

    public String getLdapUrl() {
        return String.format("%s://%s", (sslEnabled?"ldaps":"ldap"),  getDomainController());
    }
}
