# Sistema Inventario Web

Aplicación de gestión de inventario construida con Spring Boot.

## Requisitos

- Java 11 o superior
- Maven 3.6+
- Base de datos MySQL

## Instalación y Ejecución

1. Clona el repositorio
2. Configura la base de datos en `src/main/resources/application.properties`
3. Ejecuta: `mvn clean install`
4. Ejecuta: `mvn spring-boot:run`

La aplicación se conectará automáticamente a la base de datos al ejecutar el main.

## Acceso

La aplicación estará disponible en: http://localhost:8080

## Características

- Conexión automática a base de datos
- API REST integrada
- Gestión de inventario
- Logs informativos de conexión