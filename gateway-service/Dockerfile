# -------- BUILD STAGE --------
FROM maven:3.9.9-eclipse-temurin-21-jammy AS build

WORKDIR /app

# kopiujemy tylko pom.xml – cache zależności
COPY pom.xml .
RUN mvn dependency:go-offline

# kopiujemy źródła
COPY src ./src

# budujemy JAR
RUN mvn clean package -Dmaven.test.skip=true


# -------- RUNTIME STAGE --------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
