# JDK 17 image start
FROM openjdk:17
# 인자 정리 - Jar
ARG JAR_FILE=build/libs/*.jar
# jar file copy
COPY ${JAR_FILE} spillthet.jar
ENTRYPOINT ["java", "-jar", "/spillthet.jar"]