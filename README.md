# Spring Boot RabbitMQ Docker Deployment

This project demonstrates how to containerize a Spring Boot application with RabbitMQ using Docker and deploy it to Docker Hub.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Docker Deployment](#docker-deployment)
- [Docker Hub Deployment](#docker-hub-deployment)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

## 🔧 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker Desktop/Toolbox
- Docker Hub account
- Git

## 📁 Project Structure

```
RabbitMQ/
├── src/
│   ├── main/
│   │   ├── java/com/example/rabbit/
│   │   │   ├── config/RabbitMqConfig.java
│   │   │   ├── consumer/RabbitMQConsumer.java
│   │   │   ├── controller/MessageController.java
│   │   │   ├── dto/UserDto.java
│   │   │   ├── model/RabbitMQMessage.java
│   │   │   ├── producer/RabbitMQProducer.java
│   │   │   └── ProducerServiceApplication.java
│   │   └── resources/application.properties
│   └── test/
├── target/
├── Dockerfile
├── pom.xml
└── README.md
```

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/rohithparimella/RabbitMQ.git
cd RabbitMQ
```

### 2. Build the Application
```bash
# Clean and build the project
./mvnw clean package -DskipTests
```

### 3. Run Locally
```bash
# Start RabbitMQ (if not already running)
docker run -d --name rabbitmq-server -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin123 rabbitmq:3-management

# Run the Spring Boot application
./mvnw spring-boot:run
```

## 🐳 Docker Deployment

### Step 1: Create Docker Image

1. **Ensure JAR file exists:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Build Docker image:**
   ```bash
   docker build -t springboot-rabbitmq-app .
   ```

3. **Verify image creation:**
   ```bash
   docker images
   ```

### Step 2: Run with RabbitMQ

1. **Start RabbitMQ server:**
   ```bash
   docker run -d --name rabbitmq-server -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin123 rabbitmq:3-management
   ```

2. **Run Spring Boot application:**
   ```bash
   docker run -d --name springboot-app -p 8080:8080 --link rabbitmq-server:rabbitmq -e SPRING_RABBITMQ_HOST=rabbitmq -e SPRING_RABBITMQ_USERNAME=admin -e SPRING_RABBITMQ_PASSWORD=admin123 springboot-rabbitmq-app
   ```

### Step 3: Access the Application

- **Spring Boot App**: http://localhost:8080
- **RabbitMQ Management**: http://localhost:15672 (admin/admin123)

## 🏗️ Docker Hub Deployment

### Step 1: Create Docker Hub Repository

1. Go to [Docker Hub](https://hub.docker.com/)
2. Sign up/Login to your account
3. Click **"Create Repository"**
4. Provide repository name: `springboot-rabbitmq-demo`
5. Set visibility to **Public**
6. Click **"Create"**

### Step 2: Push to Docker Hub

1. **Login to Docker Hub:**
   ```bash
   docker login
   # Enter your Docker Hub username and password
   ```

2. **Tag your image:**
   ```bash
   docker tag springboot-rabbitmq-app yourusername/springboot-rabbitmq-demo:latest
   ```

3. **Push to Docker Hub:**
   ```bash
   docker push yourusername/springboot-rabbitmq-demo:latest
   ```

4. **Logout (optional):**
   ```bash
   docker logout
   ```

### Step 3: Pull and Run from Docker Hub

```bash
# Pull the image
docker pull yourusername/springboot-rabbitmq-demo:latest

# Run the image
docker run -d -p 8080:8080 yourusername/springboot-rabbitmq-demo:latest
```

## 🔌 API Endpoints

### Message Controller Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/message` | Send simple message to RabbitMQ |
| POST | `/api/v1/message/json` | Send JSON message to RabbitMQ |

### Example Requests

**Send Simple Message:**
```bash
curl -X POST http://localhost:8080/api/v1/message \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello RabbitMQ!"}'
```

**Send JSON Message:**
```bash
curl -X POST http://localhost:8080/api/v1/message/json \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "firstName": "John", "lastName": "Doe"}'
```

## ⚙️ Configuration

### Application Properties

```properties
# RabbitMQ Configuration
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Queue Configuration
rabbitmq.queue.name=rabbitmq_queue
rabbitmq.topic.name=rabbitmq_exchange
rabbit.routing.Key=rabbit_key
rabbitmq.jsonqueue.name=rabbitmq_jsonqueue
rabbit.jsonrouting.Key=rabbit_jsonkey
```

### Environment Variables for Docker

```bash
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=admin
SPRING_RABBITMQ_PASSWORD=admin123
```

## 🐛 Troubleshooting

### Common Issues

1. **Connection Refused Error:**
   ```bash
   # Check if RabbitMQ is running
   docker ps | grep rabbitmq
   
   # Check RabbitMQ logs
   docker logs rabbitmq-server
   ```

2. **Port Already in Use:**
   ```bash
   # Check which process is using the port
   lsof -i :8080
   
   # Kill the process or use different port
   docker run -p 8081:8080 springboot-rabbitmq-app
   ```

3. **JAR File Not Found:**
   ```bash
   # Ensure JAR file exists
   ls -la target/*.jar
   
   # Rebuild if necessary
   ./mvnw clean package -DskipTests
   ```

4. **Docker Build Fails:**
   ```bash
   # Check Dockerfile syntax
   docker build --no-cache -t springboot-rabbitmq-app .
   ```

### Useful Docker Commands

```bash
# View running containers
docker ps

# View all containers (including stopped)
docker ps -a

# View container logs
docker logs springboot-app

# Stop container
docker stop springboot-app

# Remove container
docker rm springboot-app

# create docker image
docker build -t springboot_rmq-demo .

# Remove image
docker rmi springboot-rabbitmq-app

# Clean up unused resources
docker system prune -a
```

## 📝 Dockerfile

```dockerfile
FROM openjdk:17.0.2
VOLUME /tmp
EXPOSE 8080
ADD target/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar"]
```

## 🔗 Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Hub](https://hub.docker.com/)

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Rohith Parimella**
- GitHub: [@rohithparimella](https://github.com/rohithparimella)

---


Steps to Create a Docker Container
🧾 Step 1: Create a Dockerfile

In your project root, create a file named Dockerfile (no extension):

# Use a base image with Java
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the built jar file into the container
COPY target/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar app.jar

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

🛠️ Step 2: Build the Docker Image
docker build -t springboot_rmq-demo .


-t is to tag the image with a name

. is the build context (current directory)

✅ This creates a Docker image named springboot_rmq-demo.

📦 Step 3: Create and Run a Container
docker run -d -p 8080:8080 --name springboot_container springboot_rmq-demo


-d runs in detached mode (in background)

-p maps ports (host:container)

--name gives your container a name

✅ This creates and starts a Docker container from your image.

🔍 Step 4: Verify Container is Running
docker ps


You'll see:

CONTAINER ID   IMAGE                PORTS                    NAMES
xyz123abc456   springboot_rmq-demo  0.0.0.0:8080->8080/tcp   springboot_container

🧪 Step 5: Access the Application

Open in your browser:

http://localhost:8080

🛑 Step 6: Stop and Remove the Container (Optional)
docker stop springboot_container
docker rm springboot_container

## 🚀 Quick Commands Summary

```bash
# Build and run locally
./mvnw clean package -DskipTests
./mvnw spring-boot:run

# Docker commands
docker build -t springboot-rabbitmq-app .
docker run -d -p 8080:8080 springboot-rabbitmq-app

# Docker Hub commands
docker login
docker tag springboot-rabbitmq-app yourusername/springboot-rabbitmq-demo:latest
docker push yourusername/springboot-rabbitmq-demo:latest
docker pull yourusername/springboot-rabbitmq-demo:latest
```

**Happy Coding! 🎉**
