FROM openjdk:8-jre
ADD /target/gateway-*.jar /gateway.jar
ENTRYPOINT ["java" , "-jar", "-Dspring.profiles.active=production", "/gateway.jar"]
