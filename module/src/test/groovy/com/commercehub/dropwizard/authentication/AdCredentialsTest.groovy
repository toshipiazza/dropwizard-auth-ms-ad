package com.commercehub.dropwizard.authentication

import io.dropwizard.auth.basic.BasicCredentials
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals
import static org.junit.Assert.assertTrue;

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

    @Test void basicCredConversion(){
        String unqualifiedUsername = RandomStringUtils.randomAlphanumeric(15)
        def ac1 = AdCredentials.fromBasicCredentials(new BasicCredentials(unqualifiedUsername, "hogan"))
        def ac2 = AdCredentials.fromBasicCredentials(new BasicCredentials("$unqualifiedUsername@$defaultDomain" as String, "hogan"))
        def ac3 = AdCredentials.fromBasicCredentials(new BasicCredentials("$defaultDomainAlias\\$unqualifiedUsername" as String, "hogan"))

        [ac1, ac2, ac3].each{
            assertEquals("sAMAccountName should match the original unqualified username", unqualifiedUsername, it.sAMAccountName)
            assertEquals("The call to getUserPrincipalName should use the default domain", String.format("%s@%s", unqualifiedUsername, defaultDomain), it.getUserPrincipalName(defaultDomain) )
        }

        def ac4 = AdCredentials.fromBasicCredentials(ac3)
        assertTrue("When asked for and AdCredential from and existing AdCredential return that instance", ac4.is(ac3) && ac3.is(ac4))

    }

}