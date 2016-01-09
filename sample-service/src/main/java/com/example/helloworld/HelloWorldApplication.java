package com.example.helloworld;

import com.commercehub.dropwizard.authentication.AdAuthenticator;
import com.commercehub.dropwizard.authentication.AdPrincipal;
import com.example.helloworld.resources.ProtectedResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }


    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {

    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws ClassNotFoundException {

        // set up ad authenticator
        AdAuthenticator<AdPrincipal> adAuthenticator = AdAuthenticator.createDefault(configuration.getAdConfiguration());

        // wrap it in a caching authenticator
        CachingAuthenticator<BasicCredentials, AdPrincipal> cachingAuthenticator =
                new CachingAuthenticator<>(environment.metrics(), adAuthenticator, configuration.getAuthenticationCachePolicy());

        // register authenticator
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<AdPrincipal>()
                    .setAuthenticator(cachingAuthenticator)
                    .setRealm("MSAD: " + configuration.getAdConfiguration().getDomain())
                    .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AdPrincipal.class));
        environment.jersey().register(new ProtectedResource());
    }
}
