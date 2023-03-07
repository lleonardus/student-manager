FROM maven AS MAVEN_BUILD
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk
WORKDIR /app
COPY --from=MAVEN_BUILD /target/student-manager-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]