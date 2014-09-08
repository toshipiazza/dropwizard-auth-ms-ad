package com.example.helloworld.resources;

import com.commercehub.dropwizard.authentication.AdPrincipal;
import com.example.helloworld.entities.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/protected")
@Produces(MediaType.APPLICATION_JSON)
public class ProtectedResource {
    @GET
    public AdPrincipal showSecret(@Auth AdPrincipal user) {
        return user;
    }
}
