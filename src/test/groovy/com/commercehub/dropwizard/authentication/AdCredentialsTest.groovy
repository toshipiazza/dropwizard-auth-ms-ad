package com.commercehub.dropwizard.authentication;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AdCredentialsTest {

    String defaultDomain = "commercehub.com";
    String defaultDomainAlias = "COMMERCEHUB";

    @Test
    public void commonBareUsername()  {
        AdCredentials cred = new AdCredentials(RandomStringUtils.randomAlphanumeric(15), "ChangeMeEtc")
        assertEquals("The sAMAccountName should be exact match for username", cred.username, cred.sAMAccountName);
        assertEquals("The call to getUserPrincipalName should use the default domain", String.format("%s@%s", cred.username, defaultDomain), cred.getUserPrincipalName(defaultDomain) )

    }

    @Test
    public void legacyQualifiedUsername()  {
        String unqualifiedUsername = RandomStringUtils.randomAlphanumeric(15)
        AdCredentials cred = new AdCredentials(defaultDomainAlias + "\\"+ unqualifiedUsername, "ChangeMeEtc")
        assertNotEquals("The sAMAccountName should not be exact match for username", cred.username, cred.sAMAccountName);
        assertEquals("The sAMAccountName should be match for unqualified username", unqualifiedUsername, cred.sAMAccountName);
        assertEquals("The call to getUserPrincipalName should use the default domain", String.format("%s@%s", unqualifiedUsername, defaultDomain), cred.getUserPrincipalName(defaultDomain) )
    }

    @Test
    public void modernQualifiedUsername()  {
        String unqualifiedUsername = RandomStringUtils.randomAlphanumeric(15)
        AdCredentials cred = new AdCredentials(unqualifiedUsername + "@" + defaultDomain, "ChangeMeEtc")
        assertNotEquals("The sAMAccountName should not be exact match for username", cred.username, cred.sAMAccountName);
        assertEquals("The sAMAccountName should be match for unqualified username", unqualifiedUsername, cred.sAMAccountName);
        assertEquals("The call to getUserPrincipalName should use the default domain", String.format("%s@%s", unqualifiedUsername, defaultDomain), cred.getUserPrincipalName(defaultDomain) )
    }

}