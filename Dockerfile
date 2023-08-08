# Step : Test and package
FROM maven:3.5.3-jdk-8-alpine as builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /build/src/
# -o flag will instruct maven to build on offline mode
RUN mvn package -DskipTests

# Step : Package image
FROM openjdk:8-jre-alpine
EXPOSE 8080
CMD exec java $JAVA_OPTS -jar /app/plateformesante-back.jar
COPY --from=builder /build/target/*.jar /app/plateformesante-back.jar