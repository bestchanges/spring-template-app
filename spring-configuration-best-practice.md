# Spring configuration best practice

This document describes technique of configuration of Spring application.

What do we want:
* out-of-the-box: it shall work with minimum or none actions required to run
* be secure: no passwords shall be in repository
* be simple: as simple as possible
* be consistent: deliver the app as Docker container (and use the same image for all installations)
* be safe: avoid occasionally run application with wrong profile (like run dev profile on production)
* be consistent: configuration files are the part of the Unit Tests and are used during build in CI
* be convenient: developer can run application from IDE (with no docker, no spring-config required)
* developer can override any configuration properties locally

## Configuration files

On testing/staging/producton environments use [spring-config](https://cloud.spring.io/spring-cloud-config/)
On development use static .properties or .yml files. Here we use .yml files, but this all is applicable to .properties files as well.

There are have two sets of configuration files: bootstrap*.yml and application*.yml.
While bootstrap*.yml files define HOW application is to be configured,
application*.yml files contain configuration settings for the application itself.

```
src/java/resources/bootstrap.yml # default profile to 'prod' and enable spring-config + provide
src/java/resources/bootstrap-dev.yml # disable spring-config for 'dev' profile
src/java/resources/application.yml
src/java/resources/application-dev.yml
bootstrap.yml # set default profile to 'dev', used when run from IDE
application.yml
```

Config files located in root directory override values from resources-based files.

Local settings for this machine can be set in application.yml in the root directory.
It is not going to be stored in git repository.

.gitignore
```
# .... other things...
/application.yml
```

## Using profiles

We use spring-environment profiles to define application run mode (dev/test/prod)

Basic configuration files bootstrap.yml and application.yml applicable to all profiles.
While profile-specific configuration files like application-prod.yml, application-dev.yml override properties from basic configuration file.

In bootstrap.yml we set default profile to 'prod' as most secure and restricted configuration.
This help us do not make any mistake while deliver to the platform.

To set active profile:
* put "spring.profiles.active: dev" in bootstrap.yml in applicatin working directory. This override basic config property.
* pass VM option "-Dspring.profile.active=dev" in Run/Debug configuration
* set system environment variable SPRING_PROFILES_ACTIVE=dev

## Pass passwords by env

We do not use [Vault](https://projects.spring.io/spring-vault/) yet, but still need to give the password to spring-config.

We use environment variables for that.

```
spring:
  cloud:
    config:
      password: ${SPRING_CLOUD_CONFIG_PASSWORD:default_password} # use env variable or set default value
```

## Keep important property in config even when it holds default value

Bright example here is "spring.cloud.config.enabled" property which controls of usage of config server.
It is kept in bootstrap.yml even having default value "true". Why? When somebody want to disable config server he needs
look to the default config and override suitable property. 
Otherwise he need to dig into documentation and find out the value by himself. 

## Use constructor with Environment instead of @Value

It's better to use constructor initialized with Environment parameter. 
This is works fine with Unit Tests, MockEnvironment and when autowired by spring.

Good:
```java
@Service
public class TimeService {

    private TimeZone timezone;
    
    public TimeService(Environment environment) {
        this.timezone = TimeZone.getTimeZone(environment.getProperty("app.TimeService.timezone"));
    }
}
```

Not good:
```java
@Service
public class TimeService {

    @Value("${app.TimeService.timezone}")
    private String timezone; // need to convert to TimeZone
    
}
```

## About multi-profiles YML

We do not use feature to store several profiles in one files although spring allows it.

Keep profile-specific staff in the profile-specific configuration files. That's it.


## Let's Docker it!

We do like docker containers, aren't we?

## Links

* https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
* https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html
