FROM adoptopenjdk/openjdk16:alpine-jre
ADD target/moneymanager-0.0.1-SNAPSHOT.war app.jar
ENTRYPOINT ["java","-jar","app.jar"]