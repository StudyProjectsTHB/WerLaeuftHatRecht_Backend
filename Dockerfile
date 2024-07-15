FROM openjdk:17
MAINTAINER WerLaeuftHatRecht@thb.de

# Kopiere die JAR-Datei in das Image
COPY ./target/mux-0.0.1-SNAPSHOT.jar /service.jar

# Arbeitsverzeichnis setzen
WORKDIR /

# Port 8080 freigeben
EXPOSE 8080

# Java-Anwendung ausführen
CMD ["java", "-jar", "/service.jar"]