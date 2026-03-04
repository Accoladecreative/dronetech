# dronetech

Spring Boot assessment project for drone medication delivery.

## Requirements

- Java 20

## Database

- In-memory H2 database (no setup required)
- Data is seeded on startup (10 drones + sample medications)
- H2 console: `http://localhost:8080/h2-console`
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

## API Endpoints

Register drone:

```bash
curl -X POST http://localhost:8080/api/drones \
  -H "Content-Type: application/json" \
  -d '{
    "serialNumber": "DRN-011",
    "model": "LIGHTWEIGHT",
    "weightLimitGrams": 200,
    "batteryCapacityPercent": 90
  }'
```

Load drone with medication items:

```bash
curl -X POST http://localhost:8080/api/drones/DRN-001/load \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      { "medicationCode": "ASPIRIN_500", "quantity": 2 },
      { "medicationCode": "AMOX_250", "quantity": 1 }
    ]
  }'
```

Get loaded medications for drone:

```bash
curl http://localhost:8080/api/drones/DRN-001/medications
```

Get available drones:

```bash
curl http://localhost:8080/api/drones/available
```

Get drone battery level:

```bash
curl http://localhost:8080/api/drones/DRN-001/battery
```

## Business Rules

- Drone total loaded weight cannot exceed `weightLimitGrams`
- Drone battery must be at least 25% for loading
- Drone must be in `IDLE` or `LOADING` state to accept new load
