# ==========================================
# STAGE 1: Build the Spring Boot JAR
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper files (or gradle files if using Gradle)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this caches dependencies if pom.xml hasn't changed)
#RUN ./mvnw dependency:go-offline

# Copy the source code and build the package
COPY src src
RUN ./mvnw package -DskipTests

# ==========================================
# STAGE 2: Run the Application
# ==========================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy only the compiled fat JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port (change if your application.properties uses a different port)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
