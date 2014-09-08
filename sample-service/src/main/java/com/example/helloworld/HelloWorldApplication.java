package com.example.helloworld;

import com.commercehub.dropwizard.authentication.AdAuthenticator;
import com.commercehub.dropwizard.authentication.AdConfiguration;
import com.commercehub.dropwizard.authentication.AdPrincipal;
import com.commercehub.dropwizard.authentication.AdPrincipalMapper;
import com.example.helloworld.entities.User;
import com.example.helloworld.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        environment.jersey().register(new BasicAuthProvider<>(AdAuthenticator.createDefault(configuration.getAdConfiguration()), "MSAD: " + configuration.getAdConfiguration().getDomain()));
        environment.jersey().register(new ProtectedResource());
    }
}
