# 🍳 Rezept-Sharing-Plattform (Kotlin & Vue.js)

Eine moderne Anwendung zum Erstellen und Teilen von Rezepten. Das Backend basiert auf Kotlin/Spring Boot und verwendet PostgreSQL für die Persistenz sowie JWT für eine stateless, sichere Benutzerauthentifizierung. Das Frontend wurde mit Vue 3 (TypeScript) entwickelt.

| Komponente | Technologie | Zweck |
| :--- | :--- | :--- |
| **Backend (API)** | **Kotlin** (Spring Boot 3) | RESTful API, Datenmodellierung (JPA), JWT-Authentifizierung. |
| **Datenbank** | **PostgreSQL** | Persistente Speicherung von Benutzer- und Rezeptdaten. |
| **Frontend (Client)** | **Vue.js** 3 (TypeScript, Vite) | Komponenten-basierte Benutzeroberfläche zur Anzeige und Verwaltung von Rezepten. |
| **Sicherheit** | **Spring Security 6** | Stateless JWT-Authentifizierung und Autorisierung. |

---

## 🚀 Lokale Entwicklung starten (Quick Start)

Um das gesamte Projekt lokal auszuführen, wird eine laufende **PostgreSQL-Datenbank** und eine .env Datei benätigt.

### 1. Voraussetzungen

Stellen Sie sicher, dass folgende Software installiert ist:

* **Java Development Kit (JDK) 17+**
* **Node.js (LTS)** und **npm** oder **yarn**
* **Docker** (oder eine lokale PostgreSQL-Installation)
* **Git**

### 2. Datenbank (PostgreSQL) starten

Der einfachste Weg, PostgreSQL zu starten, ist die Verwendung von Docker. Führen Sie diesen Befehl in Ihrem Terminal aus:

```bash
docker run --name recipe-postgres -e POSTGRES_USER=recipeuser -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=recipedb -p 5432:5432 -d postgres:latest
```

### 3. Konfigurationsdatei (.env) erstellen

Um die Zugangsdaten der Datenbank und sensible Geheimnisse (wie den JWT-Schlüssel) aus der Versionskontrolle fernzuhalten, wird im **Root-Verzeichnis** des Projekts eine `.env`-Datei angelegt.

Diese Datei definiert die Umgebungsvariablen, die von `application.properties` ausgelesen werden.

```env
# Root-Verzeichnis/.env
# -----------------------------------------------------------------------
# DATENBANK-KONFIGURATION (PostgreSQL)
# -----------------------------------------------------------------------
# URL zur Datenbank (muss mit dem Docker-Setup übereinstimmen: localhost:5432/recipedb)
DB_URL=jdbc:postgresql://localhost:5432/recipedb 
DB_USERNAME=recipeuser      
DB_PASSWORD=mysecretpassword  

# -----------------------------------------------------------------------
# SICHERHEITS-KONFIGURATION (JWT)
# -----------------------------------------------------------------------
# JWT Secret Key: Muss ein zufälliger, starker String sein (mindestens 32 Zeichen / 256 Bit)
jwt.secret.key=[]
# JWT Ablaufzeit: Zeit in Millisekunden (z.B. 86400000 ms = 24 Stunden)
jwt.expiration=86400000
```

### 4. Starten der Komponenten
A. Backend starten
In das Verzeichnis backend/ navigieren und starten der Spring Boot-Anwendung mit Gradle:

```bash
cd backend/
./gradlew bootRun
(Die API läuft jetzt unter http://localhost:8080.)
```

B. Frontend starten
Iin das Verzeichnis frontend/ navigieren & Installation der Abhängigkeiten, falls noch nicht geschehen:

```bash
cd frontend/
npm install
npm run dev
```
(Die Vue-Anwendung läuft jetzt unter http://localhost:5173 oder ähnlich.)

---

## 🗂️ Architekturübersicht

Das Projekt verwendet eine **Klassische Geschichtete Architektur (Classical Layered Architecture)**, bei der die Effizienz im Vordergrund steht. Es wird bewusst auf separate Domain- und DTO-Objekte verzichtet, um die Komplexität des Mappings zu vermeiden.

### 1. Datenmodell-Fusion (Effizienz-Kompromiss)

Um die Komplexität und den Aufwand des Mappings zu vermeiden, wurde folgende Design-Entscheidung getroffen:

* Das einzige Datenmodell dient gleichzeitig als:
    * **JPA Entity** (Datenbankdarstellung)
    * **Domain-Objekt** (Business-Logik)
    * **DTO** (Controller-Response an den Client)

### 2. Schichten und Verantwortlichkeiten

* **Controller:** Verantwortlich für HTTP-Routing, JSON-Serialisierung und die Annahme von Anfragen.
* **Service (Business Logic):** Der Kern der Anwendung. Enthält die Geschäftsregeln (z.B. Passwort-Hashing, JWT-Logik).
* **Repository (Datenzugriff):** Nutzt **Spring Data JPA**, um die Modelle ohne manuelle Implementierung direkt zu speichern und abzurufen.

**Hinweis:** Aufgrund der Fusion der Modelle ist die Service-Schicht technisch an die Datenbank-Details (JPA) gebunden. Dies ist ein bewusst gewählter Kompromiss zugunsten der Entwicklungsgeschwindigkeit.

---
