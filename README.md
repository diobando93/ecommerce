# eCommerce API

API REST para gestión de carritos de compra y productos.

## Tecnologías
- Java 17
- Spring Boot 3.2.2
- H2 Database

## Ejecución

### Opción 1: Con JAR
1. Compila la aplicación:
   ```bash
   mvn clean package
   ```
2. Ejecuta el archivo JAR generado:
   ```bash
   java -jar target/eCommerce-0.0.1.jar
   ```
3. Accede a la API en `http://localhost:8080`.

### Opción 2: Con Docker
1. Construye la imagen Docker:
   ```bash
   docker build -t ecommerce-app .
   ```
2. Ejecuta el contenedor:
   ```bash
   docker run -p 8080:8080 ecommerce-app
   ```
3. Accede a la API en `http://localhost:8080`.


## Licencia
Este proyecto está licenciado bajo la [MIT License](LICENSE).
