# 1-bosqich: build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# pom.xml ni alohida copy qilish (cache uchun)
COPY pom.xml .
RUN mvn dependency:go-offline

# source code
COPY src ./src
RUN mvn clean package -DskipTests

# 2-bosqich: run
FROM eclipse-temurin:17-jre
WORKDIR /app

# jar faylni build bosqichidan olish
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
