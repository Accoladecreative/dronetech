# dronetech

## Requirements

- Java 20

## Database

- In-memory H2 database (no setup required)
- Data is seeded on startup (10 drones + sample medications)

H2 console:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:dronetechdb`
- Username: `sa`
- Password: (leave blank)

## Run

```bash
./gradlew bootRun
```

## Test

```bash
./gradlew test
```
