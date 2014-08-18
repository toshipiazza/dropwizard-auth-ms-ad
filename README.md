# DropWizard ActiveDirectory Authentication Provider
This BasicAuth provider uses the ActiveDirectory LDAP interface to authenticate and authorize your service principals.
Existing LDAP providers can provide you the same capabilities as this Authenticator but this authenticator should require
much less configuration and can take advantage of typical behaviors used in activeDirectory deployments.

## Before you continue
This project is only in use for internal projects at CommerceHub. You should be familiar with the auth section of the DropWizard manual.
You should consult your IT administrator before you bury her carefully size AD cluster with new auth requests. You *SHOULD* cache your
interactions with AD; DropWizard provides CachingAuthenticator to help you with this.

## Maven (etc.)
Currently this module is in alpha and not available from any public maven repository. We will publish to to JCenter soon.

## Usages
Example usage

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws ClassNotFoundException {
        ...
        environment.jersey().register(new BasicAuthProvider<>(AdAuthenticator.createDefault(configuration.getAdConfiguration()), "MSAD"));
        ...
        environment.jersey().register(new ProtectedResource());

    }

## Configuration
The aim of this project is to minimize the amount of required configuration. The only REQUIRED configuration variable if *domain*

    ad:
        domain: my.company.example.com

Several additional properties can be configured, but sensible defaults should prevent you from ever needing to change them

    ad:
        domain: my.company.example.com  # No Default
        domainController: my-fav-dc.my.company.example.com # Default: <domain>
        sslEnabled: true  # Default: true
        groupResolutionMode: 1 # 0(DN) , 1(CN), or 2(sAMAccountName - EXPENSIVE). Default: 1
        usernameFilterTemplate: (&((&(objectCategory=Person)(objectClass=User)))(sAMAccountName=${username})) # Default: <As shown>
        attributeNames: # Default: <As Shown>. first two are required.
            - sAMAccountName
            - memberOf
            - mail
        requiredGroups: # Default: <empty>
            - All
            - Of
            - These
            - Are
            - Required
            - Or
            - You
            - Get
            - A
            - 403

## OK, that's cool, how about...

* ...using another cool LDAP library?
* Great idea, but for this project we do not think we should need more than the standard JRE support
* ...configuring the required group at the Resource level?
* Wonderful idea. If you get to it before us, please be sure to contribute your work.
* ...using nested groups in AD
* Right on! But it seems that resolving even the known groups from the memberOf attribute is very slow. Change the *groupResolutionMode* to 2 and see how slow.
* ...AD has this really cool feature that allows you to do X with Y!
* meh.




