# Writer's Tracker API

A Spring Boot application designed to help writers track their book progress, including chapters, plot points, and word counts. This project serves as the backend server implemented in Java.

## 🚀 Technologies Used
* **Java 17+**
* **Spring Boot 3.4.x** (Web, Security, Data JPA)
* **Database:** SQLite (Dev), PostgreSQL (Production ready)
* **Migration:** Flyway
* **Build Tool:** Maven

## 📂 Project Structure
The code follows a standard layered architecture:
* `controller`: Handles HTTP requests and responses.
* `service`: Contains business logic and validation.
* `repository`: Interfaces for database interaction.
* `model`: Database entities (e.g., User).

## 🛠️ Setup & Installation

### 1. Prerequisites
Ensure you have the following installed:
* Java Development Kit (JDK) 17 or higher
* Git

### 2. Configuration
This project uses environment variables for security.
1.  Create a `.env` file in the root directory.
2.  Add the following configuration (for local SQLite development):

## properties
`DB_URL=jdbc:sqlite:writerstracker.db`

### Clean previous builds
`./mvnw clean`

### Run the server
`./mvnw spring-boot:run`

On Windows, use `mvnw spring-boot:run.`

The server will start on port 8080.

end# writers-tracker-server
