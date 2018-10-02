FROM java:8-jre

COPY ./target/template-app.jar /app/template-app.jar
# VOLUME /app/application.yml

WORKDIR /app

CMD ["java", "-Xmx1g", "-jar", "template-app.jar"]
