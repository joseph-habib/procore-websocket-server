# Procore WebSocket Server

## Description
This repository contains the implementation of a WebSocket server using **Java** and **Spring Boot** to enable real-time, two-way communication across Procore's tools and services. The goal is to provide an in-house solution that minimizes reliance on third-party vendors, while aligning with compliance standards such as FedRAMP.

By integrating WebSocket functionality into the platform, features like collaborative editing, live conversations, and real-time updates can be efficiently delivered to end users.

---

## Features
- **WebSocket Server:** A scalable and extensible server built with Spring Boot for two-way real-time communication.
- **Kafka Integration:** Ensures reliable messaging and scalability via Apache Kafka.
- **Socket Multiplexer:** Frontend support for seamless integration of WebSocket clients.
- **FedRAMP Compliance:** Aligns with security and compliance standards.

---

## Architecture
### Components:
1. **WebSocket Server:**
   - Handles WebSocket connections and routes messages.
   - Supports message broadcasting and client-specific messaging.

2. **Kafka Topics:**
   - Acts as the backbone for communication between backend services and WebSocket clients.

3. **Frontend Socket Multiplexer:**
   - A lightweight client-side implementation to handle WebSocket connections.

---

## Getting Started

### Prerequisites
1. **Java 17** or higher installed.
2. **Apache Kafka** running locally or accessible.
3. A build tool like **Maven** or **Gradle**.

### Clone the Repository
```bash
git clone https://github.com/procore/procore-websocket-server.git
cd procore-websocket-server