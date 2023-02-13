# Technischer Setup

## Voraussetzungen

Für den Bau des Services muss JDK 11 (oder neuer), Maven 3.8.1 (oder neuer) und npm 8.11.0 (oder neuer) installiert sein:

    java -version
    mvn -version
    npm -version

Für diesen Service wird ein Frontend unter [isi-frontend](https://github.com/it-at-m/isi-frontend) bereitgestellt.

## Source Code holen
Sourcen auf den lokalen Rechner holen

    git clone https://github.com/it-at-m/isi-backend.git
    cd isi-backend

## Konfiguration des Services

Für die lokale Umgebung erstellt man die Konfigurationsdatei 

    src/main/resources/application-local.yml

Der Backend Service startet standardmäßig mit dem Port `8080` und sollte in der lokalen Umgebung umgestellt werden.

Zusätzlich wird die Datenbank, der S3 Storage mit zulässigen MIME-Types für die Dokumente und 
optional eine Anbindung an einen Identity Provider über OpenId Connect in der Konfigurationsdatei konfiguriert.

Ein Beispiel für eine Konfigurationsdatei befindet sich unter [Konfiguration lokaler Umgebung](/configuration/env/#lokale-umgebung).

Diese konfiguriert den Server Port, eine HSQL Datenbank, einen lokalen Service zur Anbindung eines S3 Storages und 
die für den Dateiupload zulässigen MIME-Types pdf und png.

## Build und Start

### Bau des Services

Der Service wird mit folgendem Befehl gebaut

    mvn clean install

### Start des Services

Im Wurzelverzeichnis befinden sich Scripts zum Start des Backend Services auf dem lokalen Rechner:

1. `runLocal.bat` bzw. `runLocal.sh`: Die Benutzer*innen müssen sich über einen Identity Provider authentifizieren.
2. `runLocalNoSecurity.bat` bzw. `runLocalNoSecurity.sh`: Die Anwendung kann ohne Authentifizierung bedient werden.

Zum Start des Backend Services wird eines der Scripts ausgeführt, z.B.:

    runLocalNoSecurity.sh