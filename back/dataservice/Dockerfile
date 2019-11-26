FROM openjdk:8-jre
ADD /target/data-service-*.jar /data-service.jar
ENTRYPOINT ["java" , "-jar", "-Dspring.profiles.active=production", "/data-service.jar"]
