package com.commercehub.dropwizard.authentication;

import com.commercehub.dropwizard.authentication.support.CaseInsensitiveHashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.commercehub.dropwizard.authentication.AdConstants.SCHEMA_ATTR_MAIL_LC;
import static com.commercehub.dropwizard.authentication.AdConstants.SCHEMA_ATTR_MEMBEROF_LC;

public class AdUtilities {

    /**
     * LDAP attributes can be challenging to deal with. The Java api only makes it more cumbersome.
     * To simply things we are taking the following simplifying assumptions.
     * 1. memberOf is always a multivalued Set
     * 2. mail is always a single value, additional values are discarded
     * 3. Other attributes that return more than one result will be treated as multivalued and will be added
     *    as a set to the returned Map
     * 4. Other attributes that return one value will be returned as Object they came back from the server as.
     *
     * Use a {@link com.commercehub.dropwizard.authentication.AdPrincipalMapper} if you want to process the resulting
     * attributes with more wisdom.
     *
     * @param attributes The raw attributes returned from the ActiveDirectory domain controller
     * @return a CaseInsensitiveHashMap with the simplified attribute model.
     */
    public static CaseInsensitiveHashMap<Object> simplify(Attributes attributes){
        CaseInsensitiveHashMap<Object> result = new CaseInsensitiveHashMap<>();
        try {
            NamingEnumeration<? extends Attribute> attrs = attributes.getAll();
            while(attrs.hasMore()){
                Attribute a = attrs.next();
                Object val;
                if(a.getID().toLowerCase().equals(SCHEMA_ATTR_MAIL_LC)){
                    val = a.get(0);
                } else if (a.size()>1 || a.getID().toLowerCase().equals(SCHEMA_ATTR_MEMBEROF_LC)){
                    Set valSet = new HashSet();
                    for(int i =0; i< a.size(); i++){
                        valSet.add(a.get(i));
                    }
                    val = valSet;
                }else{
                    val = a.get();
                }
                result.put(a.getID(), val);
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String extractDNParticle(String dn, String particle){
        String temp = dn.substring(dn.indexOf(particle + "=") + (particle.length()+1));
        return (temp.indexOf(",") > 0 ? temp.substring(0, temp.indexOf(",")) : temp).trim();
    }

    public static Set<String> extractDNParticles(Collection<String> dnStrings, String particle){
        Set<String> result = new HashSet<>();
        for(String dn: dnStrings){
            result.add(extractDNParticle(dn, particle));
        }
        return result;
    }
}
