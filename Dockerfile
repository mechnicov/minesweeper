FROM openjdk:14 AS api_builder
COPY . .
RUN ./gradlew jar

FROM alpine:latest
RUN apk add --no-cache wget tar
RUN wget https://cdn.azul.com/zulu/bin/zulu14.28.21-ca-jre14.0.1-linux_musl_x64.tar.gz
RUN tar -xvf zulu14.28.21-ca-jre14.0.1-linux_musl_x64.tar.gz
COPY --from=api_builder /build/libs/minesweeper.jar .
COPY --from=api_builder /.env .
COPY --from=api_builder /.env.test .

CMD ["/zulu14.28.21-ca-jre14.0.1-linux_musl_x64/bin/java", "-jar", "/minesweeper.jar"]
