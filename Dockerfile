# Stage 1: Build the application
FROM gradle:7.6-jdk21 AS gradle_builder

WORKDIR /app
# Copy sources
COPY . .
# Build app (jar will be in /app/build/libs)
RUN chmod +x gradlew
RUN ./gradlew clean build -x test
RUN ls -la /app/build/libs

# Stage 2: Create a lightweight image with the JAR
FROM openjdk:21-jdk-slim AS runtime

# Copy the built JAR file
WORKDIR /app
COPY --from=gradle_builder /app/build/libs/Quizz-0.0.1.jar /app/app.jar
COPY src/main/resources/application.yaml /app/application.yaml

RUN ls -la

# Specify the command to run the application
CMD ["java", "-Dspring.config.location=file:application.yaml", "-jar", "/app/app.jar"]

# Expose the port on which your app will run
EXPOSE 8080
