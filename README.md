# Spring template application

This is template for microservice spring application.
It contains best development/testing/deployment practice

## How to use it

```
$ git clone git@github.com:bestchanges/spring-template-app.git your-app
$ git remote set-url origin <your git>
```

* IDEA => open => choose pom.xml => load as project
* Ctrl+Shift+R (replace in Path) 
* Choose: In project
* replace: template-app with: your-app 
* replace: 8080 with: <your application port> 

## What's inside

* [Spring configuration best practice](spring-configuration-best-practice.md)
* simple REST service provided some time functions.
* simple scheduled Service
* Unit tests included to build
* Integration test
* Docker image creation/run/test

## TODO
* [ ] add gradle support 