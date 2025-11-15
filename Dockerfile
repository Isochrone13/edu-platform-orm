FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/Edu_Platform_ORM-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]