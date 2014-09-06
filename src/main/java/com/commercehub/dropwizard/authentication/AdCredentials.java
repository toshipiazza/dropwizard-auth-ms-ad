package com.commercehub.dropwizard.authentication;

import io.dropwizard.auth.basic.BasicCredentials;

/**
 * Convenience class to help extract userPrincipalName and sAMAccountName from passed credentials
 */
public class AdCredentials extends BasicCredentials {

    private final String sAMAccountName;

    public AdCredentials(String username, String password){
        super(username, password);
        if(username.contains("@")){
            this.sAMAccountName = username.substring(0, username.indexOf('@'));
        }else if(username.contains("\\")){
            this.sAMAccountName = username.substring(username.indexOf('\\')+1);
        }else{
            this.sAMAccountName = username;
        }
    }


    public String getsAMAccountName() {
        return sAMAccountName;
    }

    public String getUserPrincipalName(String defaultDomain) {
        return getUsername().contains("@") ? getUsername() : String.format("%s@%s", sAMAccountName, defaultDomain);
    }
}
