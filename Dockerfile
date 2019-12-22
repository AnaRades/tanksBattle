FROM  openjdk:10-jre-slim
VOLUME /tmp
COPY target/tanks-battle-game-server-0.3.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "app.jar"]