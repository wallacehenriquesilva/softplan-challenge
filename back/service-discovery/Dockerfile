FROM openjdk:8-jre
ADD /target/service-discovery-*.jar /service-discovery.jar
ENTRYPOINT ["java" , "-jar", "-Dspring.profiles.active=production", "/service-discovery.jar"]