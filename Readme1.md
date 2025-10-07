# Docker Setup Guide

This guide covers how to create Docker files, docker-compose configuration, and execute Docker containers for a Spring Boot RabbitMQ application.

## 1. Create Dockerfile

Create a `Dockerfile` in your project root:

```dockerfile
FROM openjdk:17.0.2
VOLUME /tmp
EXPOSE 8080
ADD target/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/springboot-rabbitmq-demo-0.0.1-SNAPSHOT.jar"]
```

## 2. Create docker-compose.yml

Create a `docker-compose.yml` file in your project root:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:latest
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: Test@123
      MYSQL_DATABASE: rmqdemo
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"    # RabbitMQ AMQP port
      - "15672:15672"  # RabbitMQ Management UI port
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "rabbitmq-diagnostics", "ping"]
      timeout: 20s
      retries: 10

  springboot-app:
    build: .
    container_name: rmq-Demo-springboot-app
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy
      rabbitmq:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/rmqdemo
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: Test@123
      SPRING_RABBITMQ_HOST: rabbitmq
      SPRING_RABBITMQ_PORT: 5672
    networks:
      - app-network

volumes:
  mysql-data:

networks:
  app-network:
```

## 3. Execute Docker Containers

### Build and Start All Services
```bash
# Build JAR file first
mvn clean package -DskipTests

# Build Docker image and start all services
docker-compose up --build
```

### Start Services in Background
```bash
docker-compose up -d --build
```

### Stop Services
```bash
docker-compose down
```

### Stop and Remove Volumes (Clean Reset)
```bash
docker-compose down -v
```

### View Running Containers
```bash
docker ps
```

### View All Containers (Including Stopped)
```bash
docker ps -a
```

### View Service Logs
```bash
# All services
docker-compose logs

# Specific service
docker logs rmq-Demo-springboot-app
docker logs mysql
docker logs rabbitmq
```

## 4. Access Services

| Service | URL | Credentials |
|---------|-----|-------------|
| Spring Boot API | http://localhost:8080 | - |
| RabbitMQ Management | http://localhost:15672 | guest/guest |
| MySQL | localhost:3306 | root/Test@123 |

## 5. Test API Endpoint

```bash
curl -X POST http://localhost:8080/api/publish \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello from Docker!"}'
```