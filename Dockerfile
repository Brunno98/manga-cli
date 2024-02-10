FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app
COPY ./mvnw ./mvnw
# clean up the file
RUN sed -i 's/\r$//' mvnw

COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]