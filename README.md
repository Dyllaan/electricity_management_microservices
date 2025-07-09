# Smart City Simulator


This project is a smart city simulation platform, built in Spring Boot (Java) with a React frontend.

It simulates a smart electricity lifecycle across multiple cities, with microservices for authentication, citizens, providers, and aggregation.

The system is scalable: adding a new city is as simple as configuring a new cluster.

Each city comprises:
- An **API Gateway**, which allows each microservice in the city to communicate.
- A **Citizen** microservice which simulates smart meter and manual readings from citizens of the city. Citizens readings are only stored in an in-memory database (H2).
- 3 **Providers** (A, B, C) which the Citizen microservice sends these readings to. The Citizen microservice will send the readings to a citizens single provider. These are permanently stored in a PostgreSQL database.
- The **Smart City** microservice aggregates readings from all three providers in a city and stores them in a PostgreSQL database.


To allow the React frontend to create visualisations of data across cities, a central CityGateway routes requests to the correct city based on URL. The AuthService is also available via the CityGateway and issues encrypted JWT tokens to enable access to each city. Each city is secured at the CityGateway which requests a JWT token as an Authorisation header before routing requests.

## Getting Started

### Prerequisites

- Docker installed and running
- Docker Compose

### Run the backend services

All commands below must be executed inside the `demonstration/` folder.

```bash
cd demonstration
docker-compose up
```

## Using the system

The **City Gateway** URL is:

```
http://localhost:8000
```

All requests go through this gateway, which routes to the appropriate city:

- Newcastle: `http://localhost:8000/newcastle/`
- Sunderland: `http://localhost:8000/sunderland/`
- Durham: `http://localhost:8000/durham/`
- Darlington: `http://localhost:8000/darlington/`

---

## Authentication

You must authenticate to use the simulator and services.

### Login

POST to:

```
http://localhost:8000/auth/user/login
```

with JSON body:

```json
{
  "username": "apgroup",
  "password": "password"
}
```

This will return a **JWT token**, e.g.:

```json
{
    "username": "apgroup",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGdyb3VwIiwiaWF0IjoxNzM1ODY4OTU4LCJleHAiOjE3MzU5NTUzNTh9.Ex87AB-eU-biomDleVXpAv45Q_-9Tuy-yC7pcOg-yhU"
}
```


Use this token as a **Bearer Token** in Postman or your client.

---

## Run the Simulator

### Start Simulation

POST (with Bearer token):

```
http://localhost:8000/newcastle/simulator/simulation
```

This creates users and base readings.  
It may respond `city unavailable`, but check your logs to confirm activity. Wait until it completes.

---

### Aggregate Readings

GET (with Bearer token):

```
http://localhost:8000/newcastle/smart-city/aggregate
```

This triggers aggregation.

---

### View Readings

GET (with Bearer token):

```
http://localhost:8000/newcastle/smart-city/reading
```

View all readings created.

For aggregated data (for graphs, etc.):

```
http://localhost:8000/newcastle/smart-city/reading/aggregate
```

---

## Adding a New City

Each city has its own cluster of microservices.  
The naming convention in `docker-compose.yml` must follow:

```
{CITY}-{SERVICE}
```

For example:
- `newcastle-provider-a`
- `darlington-provider-a`

API Gateway host ports per city:
- Newcastle: `8000`
- Sunderland: `8081`
- Durham: `8082`
- Darlington: `8083`

In `docker-compose.yml`, map the internal port `8080` to the host port:

```yaml
ports:
  - "8081:8080"
```

---

## Frontend

The React frontend is in a separate folder and visualises simulation results.  
You can continue development of the frontend while using the backend with Newcastle for now.

---

## Architecture

- Central Gateway → City API Gateways → Microservices
- Microservices per city:
  - Citizens generate readings
  - Providers collect readings
  - Aggregator combines data
- Auth microservice secures the system with JWT tokens
- Scalable: add more cities by duplicating and renaming the cluster

---

## Notes

- Wait for simulator and aggregator to complete and don't spam requests.
- If you encounter `city unavailable` errors, check logs.
- This is a simple, functional setup however it is not the most efficient, but it works.

# Architecture Diagram

![Architecture](architecture.png)