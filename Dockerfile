FROM openjdk:17.0.2
VOLUME /tmp
EXPOSE 8080
ADD target/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar"]