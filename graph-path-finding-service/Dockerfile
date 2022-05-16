FROM openjdk:17.0.2-jdk as build
RUN mkdir -p /usr/app
WORKDIR /usr/app
COPY ./ /usr/app
RUN ./gradlew build -x test

FROM openjdk:17.0.2-jdk
WORKDIR /usr/app
COPY --from=build /usr/app/build/libs/graph-path-finding-service-0.0.1-SNAPSHOT.jar /usr/app/graph-path-finding-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/graph-path-finding-service.jar"]
