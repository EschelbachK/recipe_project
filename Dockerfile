FROM openjdk:21
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]