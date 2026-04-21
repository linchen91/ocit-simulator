FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/ocit-simulator-1.0.0.jar app.jar

EXPOSE 8081 12001 13000 14000

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]