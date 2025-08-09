# PerfectTrip

Spring Boot multi-module project for planning smart routes.

## Build and Test

```bash
./mvnw clean test
```

## Run locally

```bash
export ORS_API_KEY=your_key_here
./mvnw -pl web spring-boot:run
```

The service listens on `http://localhost:8080` and exposes Swagger UI at `/swagger-ui`.

## Docker

```bash
./mvnw -pl web spring-boot:build-image
# then
# docker run -p 8080:8080 perfecttrip-web:0.0.1-SNAPSHOT
```
