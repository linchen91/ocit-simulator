# OCIT Traffic Simulator

Traffic Control System Simulator supporting OCIT-O, OCIT-C, and Datex II protocols.

## Features

- **OCIT-O**: Outstation protocol for traffic signal controllers
- **OCIT-C**: Center-to-center protocol for traffic management systems
- **Datex II**: European standard for traffic data exchange
- **REST API**: Full HTTP API for integration
- **Web Dashboard**: Real-time monitoring UI

## Quick Start

```bash
mvn spring-boot:run
```

Open http://localhost:8081

## Protocols

| Protocol | Port | Description |
|---------|------|-----------|
| HTTP | 8081 | REST API, Web UI |
| OCIT-O | 12001 | Outstation |
| OCIT-C | 13000 | Center-to-Center |
| Datex II | 14000 | Traffic data |

## REST Endpoints

- `/api/v1/controllers` - Traffic controllers
- `/api/v1/ocit-c/*` - OCIT-C endpoints
- `/api/v1/datex2/*` - Datex II endpoints
- `/actuator/health` - Health check

## Docker

```bash
# First build the Java application (required before Docker build)
mvn package

# Build jar and start all services
docker compose up --build

# Run in background
docker compose up -d --build

# Stop services
docker compose down
```

Open http://localhost:8081

## Build (Local)

```bash
mvn clean package
java -jar target/ocit-simulator-1.0.0.jar
```

**Troubleshooting Docker Build**

If you see `target/ocit-simulator-1.0.0.jar: not found`:
1. Run `mvn package` first to generate the jar
2. Ensure you're running `docker build` from the project root (where `target/` exists)

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- H2 Database
- TypeScript (Frontend)
- JAXB (XML)

## Java Backend Architecture

### Package Structure

```
com.ocit.simulator/
├── OcitSimulatorApplication.java     # Main entry, @EnableScheduling
├── config/
│   └── ProtocolConfig.java           # OcitProtocolHandler bean
├── model/                           # JPA Entities
│   ├── TrafficSignalController.java  # Traffic light controller
│   ├── SignalGroup.java              # Individual signal groups
│   ├── TrafficData.java            # Traffic measurements
│   └── SupplyData.java            # Controller configuration
├── service/
│   ├── SimulatorService.java       # Core business logic, in-memory store
│   └── DataSimulator.java           # Data generation/simulation
├── controller/
│   └── TrafficControllerRestController.java  # REST API /api/v1/controllers/*
└── protocol/
    ├── OcitTcpServer.java          # OCIT-O TCP server (port 12001)
    ├── OcitProtocolHandler.java    # OCIT-O XML message handling
    └── OcitMessage.java           # Message model
    └── c/                        # OCIT-C sub-package
        ├── OcitCTcpServer.java    # OCIT-C TCP server (port 13000)
        ├── OcitCProtocolHandler.java
        └── OcitCMessage.java
```

### REST Endpoints Detail

| Method | Path | Purpose |
|--------|------|--------|
| GET | `/api/v1/controllers` | List all controllers |
| GET | `/api/v1/controllers/{id}` | Get single controller |
| GET | `/api/v1/controllers/{id}/supply` | Supply configuration |
| GET | `/api/v1/controllers/{id}/traffic` | Latest traffic data |
| GET | `/api/v1/controllers/{id}/signals` | Signal groups |
| PUT | `/api/v1/controllers/{id}/status` | Update status |
| PUT | `/api/v1/controllers/{id}/signals/{gid}` | Set signal color |
| GET | `/api/v1/controllers/status` | System status |

### Data Model

**TrafficSignalController**: Munich traffic signal at fixed locations:
- Marienplatz (LSA-001)
- Odeonsplatz (LSA-002)
- Karlsplatz (LSA-003)
- Sendlinger Tor (LSA-004)
- Frauenkirche (LSA-005)

**SignalGroup**: 4 groups per controller, each with:
- signalColor: RED, YELLOW, or GREEN
- phaseDuration: cycle duration
- currentPhase: active phase number

### TCP Protocol Servers

| Server | Port | Protocol |
|--------|------|-----------|
| OcitTcpServer | 12001 | OCIT-O (outstation to controller) |
| OcitCTcpServer | 13000 | OCIT-C (center-to-center) |
| Datex2TcpServer | 14000 | Datex II (traffic data exchange) |

## Frontend Architecture

### Stack

- **Language**: TypeScript 5.3
- **Build**: `tsc` (TypeScript compiler only, no bundler)
- **Entry**: `src/main/frontend/main.ts`
- **API layer**: `src/main/frontend/api.ts`
- **Serving**: Spring Boot static files from `src/main/resources/static/`

### Source Files

| File | Purpose |
|------|---------|
| `src/main/frontend/main.ts` | App entry point, UI logic |
| `src/main/frontend/api.ts` | REST API client |
| `src/main/resources/static/index.html` | SPA shell, CSS styles |
| `src/main/resources/static/js/main.js` | Compiled JS output (gitignored) |

### UI Components

- **Status panel**: 4-column grid of system stat cards
- **OCIT-C panel**: Protocol monitoring data
- **Datex II panel**: Traffic data display
- **Controllers grid**: Auto-fill grid of controller cards with actions

### Features

- Controller CRUD (create/delete via REST)
- Signal group control via modal dialog
- Real-time protocol monitoring (OCIT-O, OCIT-C, Datex II)
- Traffic supply data visualization
- Signal color/status display

### Notes

- Pure CSS styling (no Tailwind/CSS-in-JS)
- Single-page app, no routing
- API-driven reactivity (fetch polling pattern)
- Serves compiled `/js/main.js` from static folder