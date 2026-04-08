# Build with Gradle baked into the image — avoids downloading the wrapper
# distribution from services.gradle.org (often slow / flaky on Railway).
FROM gradle:7.6.4-jdk17 AS build
WORKDIR /app

COPY . .
RUN gradle clean build -x check -x test -Pproduction --no-daemon \
    && cp "$(find build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' | head -n 1)" /app/application.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/application.jar /app/application.jar

ENV PORT=5000
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
