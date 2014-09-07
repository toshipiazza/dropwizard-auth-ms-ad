package com.commercehub.dropwizard.authentication

import org.junit.Test

import static org.junit.Assert.*

public class AdConstantsTest {


    @Test
    void checkForHumanErrorOnConstantsMaintainence(){
        ["userPrincipalName", "mail", "sAMAccountName", "memberOf"].each{
            assertEquals("Did not find a suitable attribute for $it in ${AdConstants.class.simpleName}", it, AdConstants["SCHEMA_ATTR_${it.toUpperCase()}"] )
            assertEquals("Did not find a suitable lowercase attribute for $it in ${AdConstants.class.simpleName}", it.toLowerCase(), AdConstants["SCHEMA_ATTR_${it.toUpperCase()}_LC"] )
        }
    }

}