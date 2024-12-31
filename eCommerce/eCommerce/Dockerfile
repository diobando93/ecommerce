FROM openjdk:17-jdk-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el jar de la aplicación
COPY release/eCommerce-0.0.1.jar app.jar

# Puerto expuesto por la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]