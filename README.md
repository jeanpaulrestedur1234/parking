# Sistema de Gestión de Parqueadero

API REST desarrollada con Spring Boot para la gestión de usuarios, vehículos y estancias en un parqueadero.

## Características Principales

- **Gestión de Usuarios**: Registro y actualización de propietarios.
- **Gestión de Vehículos**: Control de vehículos asociados a usuarios.
- **Control de Estancias**: Registro de entrada/salida y cálculo de cobros.
- **Event Outbox**: Patrón para garantizar la consistencia en el envío de eventos.
- **Concurrencia**: Control de bloqueos para procesos críticos de entrada/salida.

## Tecnologías

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Data JPA** (Persistencia)
- **H2 Database** (Base de datos en memoria para desarrollo/testing)
- **MapStruct** (Mapeo de DTOs)
- **Lombok** (Reducción de código repetitivo)
- **Swagger/OpenAPI** (Documentación de API)

## Requisitos

- JDK 17 o superior.
- Maven 3.6+.

## Cómo ejecutar

1. Clonar el repositorio.
2. Ejecutar el proyecto:
   ```bash
   mvn spring-boot:run
   ```
3. Acceder a la consola de H2 (opcional):
   `http://localhost:8080/h2-console`
   - **JDBC URL**: `jdbc:h2:mem:parking_db`
   - **User**: `SA`
   - **Password**: (vacío)

## Documentación de la API

Una vez en ejecución, la documentación interactiva (Swagger UI) está disponible en:
`http://localhost:8080/swagger-ui/index.html`
