package com.example.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;
import io.dropwizard.Configuration;
import com.commercehub.dropwizard.authentication.AdConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

public class HelloWorldConfiguration extends Configuration {

    private AdConfiguration adConfiguration;

    @JsonProperty("ad")
    public AdConfiguration getAdConfiguration() {
        return adConfiguration;
    }

    @JsonProperty("ad")
    public void setAdConfiguration(AdConfiguration adConfiguration) {
        this.adConfiguration = adConfiguration;
    }

    @NotEmpty
    private String authenticationCachePolicy;

    @JsonProperty
    public CacheBuilderSpec getAuthenticationCachePolicy(){
        return CacheBuilderSpec.parse(authenticationCachePolicy);
    }
}
