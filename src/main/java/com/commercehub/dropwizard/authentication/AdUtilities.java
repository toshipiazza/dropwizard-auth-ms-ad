package com.commercehub.dropwizard.authentication;

import org.apache.commons.lang.text.StrSubstitutor;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdUtilities {

    public static Map<String, Object> simplify(Attributes attributes){
        Map<String, Object> result = new com.commercehub.dropwizard.authentication.support.CaseInsensitiveHashMap<>();
        try {
            NamingEnumeration<? extends Attribute> attrs = attributes.getAll();
            while(attrs.hasMore()){
                Attribute a = attrs.next();
                Object val;
                if(a.size()==1){
                    val = a.get();
                }else{
                    Set valSet = new HashSet();
                    for(int i =0; i< a.size(); i++){
                        valSet.add(a.get(i));
                    }
                    val = valSet;
                }
                result.put(a.getID(), val);
            }

        } catch (NamingException e) {
            //TODO
            e.printStackTrace();
        }
        return result;
    }

    public static String expandTemplate(String template, Map<String, String> values) {
        return StrSubstitutor.replace(template, values, "${", "}");
    }
}
