# Stage 1: Build dengan Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy semua file project
COPY pom.xml .
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# Stage 2: Run aplikasi
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy jar hasil build
COPY --from=build /app/target/*.jar app.jar

# Expose port aplikasi
EXPOSE 8080

# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]
