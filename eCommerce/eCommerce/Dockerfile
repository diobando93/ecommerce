# Usa una imagen base ligera de OpenJDK para Java 17
FROM openjdk:17-jdk-slim

# Define un argumento para el archivo JAR
ARG JAR_FILE=target/ecommerce.jar

# Copia el JAR generado al contenedor
COPY ${JAR_FILE} app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]