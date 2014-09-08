package com.example.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import com.commercehub.dropwizard.authentication.AdConfiguration;

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
}
