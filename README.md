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

### 1. Voraussetzungen

Folgende Software muss installiert sein:

* **Java Development Kit (JDK) 17+**
* **Node.js (LTS)** und **npm** oder **yarn**
* **Docker** (oder eine lokale PostgreSQL-Installation)
* **Git**

### 2. Konfigurationsdatei (.env) erstellen

Um die Zugangsdaten der Datenbank und sensible Geheimnisse (wie den JWT-Schlüssel) aus der Versionskontrolle fernzuhalten, wird im **Root-Verzeichnis** des Projekts eine `.env`-Datei angelegt.

```env
# Root-Verzeichnis/.env
# -----------------------------------------------------------------------
# DATENBANK-KONFIGURATION (PostgreSQL)
# -----------------------------------------------------------------------
# Hinweis: Die Host-Adresse ist vom Ausführungskontext abhängig.

# URL für LOKALES Debugging (Wird für Option A verwendet)
DB_URL=jdbc:postgresql://localhost:5432/postgres

# URL für DOCKER COMPOSE (Wird für Option B verwendet)
DB_URL=jdbc:postgresql://db:5432/postgres 
DB_NAME=postgres # Wird von Docker Compose für POSTGRES_DB verwendet

# Zugangsdaten (Müssen konsistent sein, hier: Standard-Postgres-User)
DB_USERNAME=postgres      
DB_PASSWORD=mysecretpassword 

# -----------------------------------------------------------------------
# SICHERHEITS-KONFIGURATION (JWT)
# -----------------------------------------------------------------------
# JWT Secret Key: Muss ein zufälliger, starker String sein (mindestens 32 Zeichen / 256 Bit)
JWT_SECRET_KEY=[BITTE HIER EINEN LANGEN, ZUFÄLLIGEN GEHEIMSCHLÜSSEL EINFÜGEN]
# JWT Ablaufzeit: Zeit in Millisekunden (z.B. 86400000 ms = 24 Stunden)
JWT_EXPIRATION=86400000
```

### 🚀 3. Startoptionen

### Option A: 💻 Lokales Development (Debugging)

Dieser Modus dient dem schnellen Code-Test und Debugging. Erfordert, dass die Datenbank **manuell** über `docker run` oder eine lokale Installation gestartet wird.

* **Vorbereitung:** Starten der PostgreSQL-Datenbank.
```bash
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=postgres -p 5432:5432 -d postgres:latest
```
* **Backend starten:** Navigieren zu `backend/` und Anwendung starten in IDE oder Shell. **Wichtig:** Die Umgebungsvariable `DB_URL` muss auf `jdbc:postgresql://localhost:5432/recipedb` gesetzt werden.
* **Frontend starten:** Navigieren zu `frontend/` und `npm run dev` ausführen.

### Option B: 🐳 Container-basiertes Deployment (Docker Compose)

* **Startbefehl:** Starten der Applikation per docker compose
    ```bash
    docker compose up --build -d
    ```
    *(Die Anwendung ist über `http://localhost:5173` erreichbar.)*

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
