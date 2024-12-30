#!/bin/bash
echo "Building application with Maven..."
mvn clean package
echo "Building Docker image..."
docker build -t ecommerce-app .
echo "Starting application..."
docker run -p 8080:8080 ecommerce-app