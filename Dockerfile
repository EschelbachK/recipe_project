FROM openjdk:21
EXPOSE 8080
ADD 
ENTRYPOINT ["java", "-jar", "app.jar"]