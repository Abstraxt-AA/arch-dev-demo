FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder
WORKDIR application
COPY ./ .
RUN mvn clean package
RUN mv target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --launcher --layers --destination app

FROM eclipse-temurin:21.0.4_7-jre-noble
WORKDIR application
COPY --from=builder application/app/dependencies/ ./
COPY --from=builder application/app/spring-boot-loader/ ./
COPY --from=builder application/app/snapshot-dependencies/ ./
COPY --from=builder application/app/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

