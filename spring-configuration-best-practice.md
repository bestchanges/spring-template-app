# Spring configuration best practice

This document describes technique of configuration of Spring application.

What we want:
* work out of the box with minimum or none actions required to run
* be secure: No passwords shall be in repository
* be simple: as simple as possible
* be consistent: deliver the app as Docker container (use the same image for all platforms)
* be safe: avoid occasionally run application with wrong profile (like run dev profile on production)
* be consistent: configuration is part of Unit Tests and used during build in CI
* be convenient: developer can run application right from IDE (no docker, no spring-config required)
* developer can override any configuration properties locally

## Configuration files

On testing/staging/producton environments automatically config with [spring-config](https://cloud.spring.io/spring-cloud-config/)
On development will use static .properties or .yml files. Here we use .yml files, but this all is applicable to .properties files as well.

There are have two sets of configuration files: bootstrap*.yml and application*.yml.
While bootstrap*.yml files define HOW application is to be configured,
application*.yml files contain configuration settings for the application itself.

Config files located in root directory overrides values from resources files.

```
src/java/resources/bootstrap.yml # default profile to 'prod' and enable spring-config + provide
src/java/resources/bootstrap-dev.yml # disable spring-config for 'dev' profile
src/java/resources/application.yml
src/java/resources/application-dev.yml
bootstrap.yml # set default profile to 'dev' when run locally from IDE
application.yml # temporally settings for this local machine (this file in .gitignore)
```

.gitignore
```
# .... other things...
application.yml
```

src/java/resources/bootstrap.yml:
```yaml

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

We do not use [Vault](https://projects.spring.io/spring-vault/), but still need to give the password to spring-config application.

We use environment variables for that.

The varable itsellf can be set in docker-compose.yml.

```
spring:
  cloud:
    config:
      password: ${SPRING_CLOUD_CONFIG_PASSWORD:default_password} # use env variable or set default value
```

## About multi-profiles YML

We do not use feature to store several profiles in one files although spring allows it.

Keep profile-specific staff in the profile-specific configuration files. That's it.


## Let's Docker it!

We do like docker containers, aren't we?

## Links

* https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
* https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html
