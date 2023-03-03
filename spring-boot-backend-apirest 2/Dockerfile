FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/spring-boot-backend-apirest-0.0.1-SNAPSHOT.jar spring-boot-backend-apirest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/spring-boot-backend-apirest-0.0.1-SNAPSHOT.jar"]