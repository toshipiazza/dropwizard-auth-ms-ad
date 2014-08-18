package com.commercehub.dropwizard.authentication;

public class PassThroughAdPrincipalMapper implements AdPrincipalMapper<AdPrincipal> {



    @Override
    public AdPrincipal map(AdPrincipal adPrincipal) {
        return adPrincipal;
    }
}
