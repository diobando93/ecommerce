# eCommerce API

API REST para gestión de carritos de compra y productos.

## Tecnologías
- Java 17
- Spring Boot 3.2.2
- H2 Database

## Ejecución

### Opción 1: Con JAR
```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/eCommerce-0.0.1.jar


### Opción 2: Con Docker
# Construir imagen
docker build -t ecommerce-app .

# Ejecutar contenedor
docker run -p 8080:8080 ecommerce-app
