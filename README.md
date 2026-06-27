# AUI Lab — Album & Song Microservices

A small microservices system for managing music albums and songs, built as a university lab project to practice service-to-service communication, API design, and containerization.

## Project Overview

The system is split into independent Spring Boot services that communicate with each other over REST, plus an Angular frontend for interacting with the API:

- **`album-service`** — manages albums (name, author, year of release). Exposes endpoints to list, create, update, and delete albums, and to fetch an album's songs.
- **`song-service`** — manages songs (name, duration, associated album). Exposes endpoints to list, create, update, and delete songs, plus bulk operations scoped to an album.
- **`gateway-service`** — built with **Spring Cloud Gateway**. Routes `/api/albums/**` to `album-service` and `/api/songs/**` to `song-service` based on path predicates, with CORS configured globally so the frontend can call the API directly.
- **`frontend`** — an Angular application providing a simple UI to call the API.

Each service runs in its own Docker container and uses an embedded **H2** database, with the whole system orchestrated via `docker-compose`.

---

## Key Design Point: Two-Way Service Communication

What makes this project interesting compared to a typical one-directional microservice demo is that **both services call each other**:

- `album-service` calls `song-service` (via a `SongClient`) to fetch, update, or delete all songs belonging to an album — e.g. when an album is renamed, its songs are updated to reflect the new album name; when an album is deleted, its songs are deleted too.
- `song-service` calls `album-service` (via an `AlbumClient`) to validate that an album exists before a song can be created or moved to it, and to keep a denormalized copy of the album name on each song for fast reads.

This required thinking about consistency between services without a shared database — each service owns its own data and talks to the other only through its REST API.

---

## Technologies Used

- **Java** with **Spring Boot** (Spring Web, Spring Data JPA)
- **Spring Cloud Gateway** — declarative request routing to downstream services
- **H2** — in-memory/embedded database, one per service
- **REST clients** for service-to-service communication
- **Angular** (TypeScript), served via **Nginx** — frontend for interacting with the API
- **Docker / Docker Compose** — containerizing and orchestrating all services together

---

## API Overview

### Album Service — `/api/albums`

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/albums` | List all albums |
| GET | `/api/albums/{id}` | Get a single album |
| GET | `/api/albums/{id}/songs` | Get all songs for an album (calls `song-service`) |
| POST | `/api/albums` | Create an album (rejects duplicate names) |
| PUT | `/api/albums/{id}` | Update an album (propagates name changes to its songs) |
| DELETE | `/api/albums/{id}` | Delete an album (also deletes its songs via `song-service`) |

### Song Service — `/api/songs`

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/songs` | List all songs |
| GET | `/api/songs/{id}` | Get a single song |
| POST | `/api/songs` | Create a song (validates the album exists via `album-service`) |
| PUT | `/api/songs/{id}` | Update a song (re-validates album if changed) |
| DELETE | `/api/songs/{id}` | Delete a song |
| GET | `/api/songs/album/{albumId}` | List all songs belonging to an album |
| PUT | `/api/songs/album/{albumId}` | Bulk-update the album name on all of an album's songs |
| DELETE | `/api/songs/album/{albumId}` | Bulk-delete all songs belonging to an album |

All requests to either service can also be routed through `gateway-service`.

---

## Running the Project

### Prerequisites

- Docker and Docker Compose

### Steps

1. Clone the repository.
2. From the project root, start everything with:
   ```
   docker compose up --build
   ```
3. This builds and starts `album-service`, `song-service`, and `gateway-service`, each in its own container.
4. The Angular frontend is served via **Nginx** in its own container — once `docker-compose up` finishes, it's available in the browser. Alternatively, call the REST endpoints directly (e.g. via Postman/Bruno) through the gateway on port `8080`.

---

*This project was developed as a university lab exercise.*
