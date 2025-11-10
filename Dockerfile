FROM maven:3.9.11 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src/ ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/cardGame-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]