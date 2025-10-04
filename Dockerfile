FROM openjdk:21-jdk-slim
COPY build/libs/blog-reactive-latest.jar /app.jar
CMD ["java", "-jar", "/app.jar"]