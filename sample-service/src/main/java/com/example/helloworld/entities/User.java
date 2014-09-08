package com.example.helloworld.entities;

import java.util.Set;

public class User {

    String username;
    Set<String> roles;

    public User(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }


    public Set<String> getRoles() {
        return roles;
    }

}
