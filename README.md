# DropWizard ActiveDirectory Authentication Provider

## Introduction [![Build Status](https://travis-ci.org/commercehub-oss/dropwizard-auth-ms-ad.svg?branch=master)](https://travis-ci.org/commercehub-oss/dropwizard-auth-ms-ad)
This BasicAuth provider uses the ActiveDirectory LDAP interface to authenticate and authorize your service principals.
Existing LDAP providers can provide you the same capabilities as this Authenticator but this authenticator should require
much less configuration and can take advantage of typical behaviors used in activeDirectory deployments.

## Before you continue
This project is only in use for internal projects at CommerceHub. You should be familiar with the auth section of the DropWizard manual.
You should consult your IT administrator before you bury her carefully size AD cluster with new auth requests. You *SHOULD* cache your
interactions with AD; DropWizard provides CachingAuthenticator to help you with this.

## Maven (etc.) [ ![Download](https://api.bintray.com/packages/commercehub-oss/main/dropwizard-auth-active-directory/images/download.png) ](https://bintray.com/commercehub-oss/main/dropwizard-auth-active-directory/_latestVersion)

Maven



```xml

   ...
   <repositories>
       <repository>
         <id>jcenter</id>
         <url>http://jcenter.bintray.com</url>
       </repository>
     </repositories>

   ...

   <dependency>
       <groupId>com.commercehub.dropwizard</groupId>
       <artifactId>dropwizard-auth-active-directory</artifactId>
       <version>0.2.1</version>
   </dependency>
```
Gradle

```groovy

    ...
    repositories {
        jcenter()
    }

    ...
    dependencies {
        ...
        compile(group: 'com.commercehub.dropwizard', 
        name: 'dropwizard-auth-active-directory', 
        version: '0.2.3', 
        ext: 'jar')
        ...
    }

```

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
The aim of this project is to minimize the amount of required configuration. The only REQUIRED configuration variable is *domain*

    ad:
        domain: my.company.example.com

Several additional properties can be configured, but sensible defaults should prevent you from ever needing to change them
```yaml
    ad:
        domain: my.company.example.com  # No Default
        domainController: my-fav-dc.my.company.example.com # Default: <domain>
        sslEnabled: true  # Default: true
        usernameFilterTemplate: (&((&(objectCategory=Person)(objectClass=User)))(sAMAccountName=%s)) # Default: <As shown> %s replaced with the sAMAccountName
        attributeNames: # Default: <As Shown>. first two are required.
            - sAMAccountName
            - memberOf
            - mail
        connectionTimeout: 1000 # Default: as shown in millseconds
        readTimeout: 1000 # Default: as shown in millseconds
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
```

## Sample Service
This project includes a sample dropwazard service. Simply clone the repo, update sample-service/config/dev.yaml to
point to your domain then run

    ./gradlew run -PdwArgs='server,config/dev.yaml' -Ddw.ad.domain=nexus.commercehub.com

Then hit http://localhost:8080/protected and provide your username and password.


## OK, that's cool, how about...

* ...using another cool LDAP library?
* Great idea, but for this project we do not think we should need more than the standard JRE support
* ...configuring the required group at the Resource level?
* Wonderful idea. If you get to it before us, please be sure to contribute your work.
* ...using nested groups in AD
* Right on! But it seems that resolving even the known groups from the memberOf attribute is very slow. I am sure there
is a clever highly performant way to do it, find it and let us know.
* ...AD has this really cool feature that allows you to do X with Y!
* meh.




