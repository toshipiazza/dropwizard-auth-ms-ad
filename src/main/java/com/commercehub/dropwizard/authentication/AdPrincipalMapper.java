package com.commercehub.dropwizard.authentication;

public interface AdPrincipalMapper<T> {
    public static final AdPrincipalMapper<AdPrincipal> DEFAULT = new PassThroughAdPrincipalMapper();

    public T map(AdPrincipal adPrincipal);
}
