FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} SHBRProject.jar
ENTRYPOINT ["java","-jar","SHBRProject-1.0.jar"]