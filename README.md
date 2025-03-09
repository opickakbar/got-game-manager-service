# GameManagerService

## Overview
The **GameManagerService** manages the overall game flow, starting the game and retrieving results. It acts as the main orchestrator, ensuring players take turns and determining the winner.

## Features
- **Game Initialization**: Starts the game by generating a random number and sending it to Player 1.
- **Winner Retrieval**: Fetches the game result from **Redis** once a player wins.
- **RabbitMQ Integration**: Uses event-driven messaging to manage game moves.

## Technologies Used
- **Java 21**
- **Spring Boot** (REST API, Dependency Injection)
- **Spring AMQP (RabbitMQ)** (Message Queue for Communication)
- **Redis** (Storing Game State & Winner Information)
- **JUnit & Mockito** (Unit Testing)

## Project Structure
```
GameManagerService
├── config
│   ├── RabbitConfig.java
├── controller
│   ├── GameController.java
├── service
│   ├── GameManagerService.java
│   ├── GameRedisManager.java
├── dto
│   ├── GameMoveEventDto.java
├── util
│   ├── Utils.java
```

## Running the Service
Ensure **RabbitMQ** and **Redis** are running before starting the service.

### Steps to Run:
1. **Start RabbitMQ & Redis**
   ```sh
   docker-compose up --build
   ```
2. **Run GameManagerService**
   ```sh
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8091"
   ```

## API Endpoints
### 1. Start Game
**Endpoint:** `POST /games/start`

**Description:**
- Generates a random number between **10 and 100**.
- Sends the first move event to **Player 1** via RabbitMQ.

**Response Example:**
```json
"Game Started with number: 42"
```

### 2. Get Game Result
**Endpoint:** `GET /games/result`

**Description:**
- Retrieves the winner from Redis if the game has ended.

**Response Example (Winner Exists):**
```json
"Winner is Player1"
```
**Response Example (No Winner Yet):**
```json
"Game is not finished yet!"
```

## Running Tests
To execute unit tests:
```sh
mvn test
```
Expected output:
```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
---

Please check [here](./GAME_DETAILS.md) to see game details,

Made with ❤️ by Muhammad Taufik Akbar
