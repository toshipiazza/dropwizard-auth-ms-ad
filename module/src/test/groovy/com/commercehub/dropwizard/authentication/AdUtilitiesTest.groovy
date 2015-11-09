package com.commercehub.dropwizard.authentication

import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.*

public class AdUtilitiesTest {

    @Test
    public void dnParticleExtraction()  {
        def dn = "cn=one,ou= two ,dc=commercehub,dc=com"
        assertEquals("could not extract cn", 'one', AdUtilities.extractDNParticle(dn, "cn"));
        assertEquals("could not extract ou", 'two', AdUtilities.extractDNParticle(dn, "ou"));
        assertEquals("could not extract dc", 'commercehub', AdUtilities.extractDNParticle(dn, "dc"));
    }

    @Test
    public void dnParticleExtraction_null() {
        def dn = "cn=one,ou= two ,dc=commercehub,dc=com"
        assertNull(AdUtilities.extractDNParticle(null, null));
        assertNull(AdUtilities.extractDNParticle(null, "cn"));
        assertNull(AdUtilities.extractDNParticle(dn, null));
        assertNull(AdUtilities.extractDNParticle(dn, "xx"));
    }

    @Test
    public void dnCaseInsensitiveParticleExtraction()  {
        def dn = "CN=one,OU= two ,DC=commercehub,DC=com"
        assertEquals("could not extract cn", 'one', AdUtilities.extractDNParticle(dn, "cn"));
        assertEquals("could not extract ou", 'two', AdUtilities.extractDNParticle(dn, "ou"));
        assertEquals("could not extract dc", 'commercehub', AdUtilities.extractDNParticle(dn, "dc"));
    }

    @Test
    public void dnParticlesExtraction()  {
        //This list has purposefully messy examples
        def dns = ["cn=one,dc=commercehub,dc=com","fo=bar,cn=two,dc=commercehub,dc=com","jo=king,cn=three ,o=has-space,dc=commercehub,dc=com",]
        assertEquals("Could not extract cn's from list", ["one", "two", "three"] as Set, AdUtilities.extractDNParticles(dns, "cn"));
    }

    @Test
    public void dnParticlesExtraction_Null() {
        def dns = ["cn=one,dc=commercehub,dc=com","fo=bar,cn=two,dc=commercehub,dc=com","jo=king,cn=three ,o=has-space,dc=commercehub,dc=com",]
        assertEquals(0, AdUtilities.extractDNParticles(null, null).size());
        assertEquals(0, AdUtilities.extractDNParticles(null, "cn").size());
        assertEquals(0, AdUtilities.extractDNParticles(dns, null).size());
        assertEquals(0, AdUtilities.extractDNParticles(dns, "xx").size());
    }

    @Test
    public void checkMisses(){
        def dn = "cn=one,ou= two ,dc=commercehub,dc=com"
        assertEquals("Hit-miss case should have returned a null", null, AdUtilities.extractDNParticle(dn, "jo"));
        assertEquals("Hit-miss case should have returned empty Set", [] as Set, AdUtilities.extractDNParticles([dn], "jo"));
    }

    @Test
    public void falseHit(){
        //@ToDo this case should be handled correctly
        def dn = "cn=one,ou=two,u=three,dc=commercehub,dc=com"
        assertEquals("could not extract u", 'three', AdUtilities.extractDNParticle(dn, "u"));
    }
}