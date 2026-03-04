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

#### **Health Check**
`GET /health`

```bash
curl http://localhost:8080/health
```

Sample response (`200 OK`):

```json
{
  "status": "ok"
}
```

#### **Register Drone**
`POST /api/drones`

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

Sample response (`201 Created`):

```json
{
  "serialNumber": "DRN-011",
  "model": "LIGHTWEIGHT",
  "weightLimitGrams": 200,
  "batteryCapacityPercent": 90,
  "state": "IDLE"
}
```

Sample response (`409 Conflict`, duplicate serial):

```json
{
  "error": "Conflict",
  "message": "Drone with serial number 'DRN-011' already exists.",
  "timestamp": "2026-03-04T13:59:10Z",
  "path": "/api/drones"
}
```

#### **Load Drone With Medications**
`POST /api/drones/{serialNumber}/load`

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

Sample response (`200 OK`):

```json
{
  "serialNumber": "DRN-001",
  "model": "LIGHTWEIGHT",
  "weightLimitGrams": 100,
  "batteryCapacityPercent": 95,
  "state": "LOADED"
}
```

Sample response (`409 Conflict`, low battery):

```json
{
  "error": "Conflict",
  "message": "Drone battery level is below 25%, loading is not allowed.",
  "timestamp": "2026-03-04T13:59:40Z",
  "path": "/api/drones/DRN-010/load"
}
```

Sample response (`409 Conflict`, overweight):

```json
{
  "error": "Conflict",
  "message": "Total medication weight exceeds drone weight limit.",
  "timestamp": "2026-03-04T13:59:52Z",
  "path": "/api/drones/DRN-001/load"
}
```

Sample response (`404 Not Found`, medication missing):

```json
{
  "error": "Not Found",
  "message": "Medication with code 'UNKNOWN_MED' was not found.",
  "timestamp": "2026-03-04T14:00:02Z",
  "path": "/api/drones/DRN-001/load"
}
```

#### **Get Loaded Medications For Drone**
`GET /api/drones/{serialNumber}/medications`

```bash
curl http://localhost:8080/api/drones/DRN-001/medications
```

Sample response (`200 OK`):

```json
[
  {
    "medicationCode": "ASPIRIN_500",
    "medicationName": "Aspirin_500",
    "weightGrams": 20,
    "quantity": 2,
    "totalWeightGrams": 40
  },
  {
    "medicationCode": "AMOX_250",
    "medicationName": "Amox-250",
    "weightGrams": 30,
    "quantity": 1,
    "totalWeightGrams": 30
  }
]
```

Sample response (`404 Not Found`, drone missing):

```json
{
  "error": "Not Found",
  "message": "Drone with serial number 'DRN-999' was not found.",
  "timestamp": "2026-03-04T14:00:22Z",
  "path": "/api/drones/DRN-999/medications"
}
```

#### **Get Available Drones**
`GET /api/drones/available`

```bash
curl http://localhost:8080/api/drones/available
```

Sample response (`200 OK`):

```json
[
  {
    "serialNumber": "DRN-001",
    "model": "LIGHTWEIGHT",
    "weightLimitGrams": 100,
    "batteryCapacityPercent": 95,
    "state": "IDLE"
  },
  {
    "serialNumber": "DRN-005",
    "model": "CRUISERWEIGHT",
    "weightLimitGrams": 350,
    "batteryCapacityPercent": 54,
    "state": "IDLE"
  }
]
```

#### **Get Drone Battery Level**
`GET /api/drones/{serialNumber}/battery`

```bash
curl http://localhost:8080/api/drones/DRN-001/battery
```

Sample response (`200 OK`):

```json
{
  "serialNumber": "DRN-001",
  "batteryCapacityPercent": 95
}
```

Sample response (`404 Not Found`):

```json
{
  "error": "Not Found",
  "message": "Drone with serial number 'DRN-404' was not found.",
  "timestamp": "2026-03-04T14:00:49Z",
  "path": "/api/drones/DRN-404/battery"
}
```

#### **Get Drone Battery Audit Logs**
`GET /api/drones/{serialNumber}/battery/audit?limit=50`

```bash
curl "http://localhost:8080/api/drones/DRN-001/battery/audit?limit=50"
```

Sample response (`200 OK`):

```json
[
  {
    "droneSerialNumber": "DRN-001",
    "batteryCapacityPercent": 95,
    "checkedAt": "2026-03-04T13:57:00Z"
  },
  {
    "droneSerialNumber": "DRN-001",
    "batteryCapacityPercent": 95,
    "checkedAt": "2026-03-04T13:56:00Z"
  }
]
```

Sample response (`400 Bad Request`, invalid limit):

```json
{
  "error": "Bad Request",
  "message": "getBatteryAudit.limit: must be greater than or equal to 1",
  "timestamp": "2026-03-04T14:01:10Z",
  "path": "/api/drones/DRN-001/battery/audit"
}
```

## Business Rules

- Drone total loaded weight cannot exceed `weightLimitGrams`
- Drone battery must be at least 25% for loading
- Drone must be in `IDLE` or `LOADING` state to accept new load

## Battery Audit Job

- A scheduled job runs every 1 minute and logs battery snapshots for all drones.
- Interval is configurable via `dronetech.audit.fixed-rate-ms` in `application.yml`.
