# GameManagerService

## Overview
The **GameManagerService** is responsible for managing the game flow, starting the game, and retrieving the results. It acts as the central controller that initiates the gameplay and determines the winner.

## Features
- **Starts the game** by generating a random initial number and sending it to Player 1.
- **Retrieves the winner** from Redis when the game ends.
- **Uses RabbitMQ** for event-driven communication between players.

## Project Structure
```
GameManagerService
├── controller
│   ├── GameController.java
├── service
│   ├── GameManagerService.java
│   ├── GameRedisManager.java
├── dto
│   ├── GameMoveEventDto.java
├── util
│   ├── Utils.java
├── config
│   ├── RabbitConfig.java
```

## API Endpoints

### 1. Start Game
**Endpoint:** `POST /games/start`  
**Description:**
- Initializes the game with a random number between 10 and 100.
- Sends the first move event to Player 1 via RabbitMQ.

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

## Technologies Used
- **Spring Boot** (REST API, Dependency Injection)
- **Spring AMQP** (RabbitMQ for event-based communication)
- **Redis** (Caching game state and storing the winner)
- **JUnit & Mockito** (Unit Testing)

## How It Works
1. **Players register** in their respective services.
2. **GameManagerService starts the game** and sends the first number to Player 1.
3. **Players take turns processing moves** and sending them to each other via RabbitMQ.
4. **When a player reaches 1**, the game stores the winner in Redis.
5. **GameManagerService fetches the result** when requested.

## Running the Service
Ensure you have **RabbitMQ** and **Redis** running before starting the service.

### Steps to Run This Game:
1. **Start RabbitMQ** and **Redis**.
   ```sh
   docker-compose up --build
   ```
2. **Run the GameManagerService:**
   ```sh
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8091"
   ```
3. **Start the [Player Service](https://github.com/opickakbar/got-player-service)**:

   **Running Player 1:**
   ```sh
   cd /got-player-service
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
   ```
   **Running Player 2:**
   ```sh
   cd /got-player-service
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
   ```

4. Use **API calls** to start and check game results.

---

Made with ❤️ Muhammad Taufik Akbar

