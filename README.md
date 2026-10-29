# AutoAndes API — Taller Mecánico

Backend REST empresarial desarrollado para el Examen Parcial II de Lenguaje de Programación II (Ciclo IV, Semestre 2026-2) en la Universidad Peruana Unión (UPeU).

## 🚀 Tecnologías
- **Java 21**
- **Spring Boot 4.1.2** (Web, Data JPA, Security, Validation)
- **Oracle Database 23c / 21c (FREEPDB1)**
- **Spring Security 6 (Stateless) + JJWT 0.12.6**
- **OpenAPI 3 / Swagger UI** (SpringDoc 3.2.0)
- **Lombok**

---

## 🛠️ Instrucciones de Ejecución

### 1. Requisitos Previos
- Oracle Database corriendo localmente en el puerto `1522` (PDB: `FREEPDB1`).
- Contenedor / Usuario configurado:
```sql
ALTER SESSION SET CONTAINER = FREEPDB1;
CREATE USER autoAndeBackEnd IDENTIFIED BY 123456;
GRANT ALL PRIVILEGES TO autoAndeBackEnd;
ALTER USER autoAndeBackEnd QUOTA UNLIMITED ON USERS;
GRANT CONNECT, RESOURCE, DBA TO autoAndeBackEnd;
```

### 2. Iniciar el Backend
Desde la raíz del proyecto `AutoAndesBackEnd`:
```bash
# En Windows PowerShell o CMD
.\mvnw.cmd spring-boot:run
```
La aplicación se iniciará en el puerto `8080`.

### 3. Documentación Swagger / OpenAPI
Una vez iniciado el servicio, accede a la documentación interactiva:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 👥 Usuarios de Prueba (Anexo 10)

| Usuario | Contraseña | Rol | Permisos Principales | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **admin** | `Auto2026*` | `ADMIN` | Acceso irrestricto a todas las operaciones | Activo |
| **asesor** | `Auto2026*` | `ASESOR` | Registro de vehículos y órdenes, lectura de servicios y clientes | Activo |
| **jefe** | `Auto2026*` | `JEFE_TALLER` | Gestión de servicios, anulación de órdenes, consulta de reportes | Activo |
| **inactivo** | `Auto2026*` | `ASESOR` | Cuenta suspendida (valida respuesta 401 en login) | Inactivo |

---

## 📜 Contrato del API REST (`/api/v1`)

| Verbo | Ruta | Permiso Requerido | Códigos HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Público | `200`, `400`, `401` |
| `GET` | `/api/v1/auth/me` | Autenticado | `200`, `401` |
| `GET` | `/api/v1/clientes` | `VEHICULO_LEER` | `200`, `401`, `403` |
| `GET` | `/api/v1/servicios` | `SERVICIO_LEER` | `200`, `401`, `403` |
| `GET` | `/api/v1/servicios/activos` | `SERVICIO_LEER` | `200`, `401`, `403` |
| `POST` | `/api/v1/servicios` | `SERVICIO_ESCRIBIR` | `201`, `400`, `401`, `403`, `409` |
| `PUT` | `/api/v1/servicios/{id}` | `SERVICIO_ESCRIBIR` | `200`, `400`, `401`, `403`, `404`, `409` |
| `PATCH`| `/api/v1/servicios/{id}/estado` | `SERVICIO_ESCRIBIR` | `200`, `401`, `403`, `404` |
| `GET` | `/api/v1/vehiculos` | `VEHICULO_LEER` | `200`, `401`, `403` |
| `POST` | `/api/v1/vehiculos` | `VEHICULO_ESCRIBIR` | `201`, `400`, `401`, `403`, `409` |
| `PUT` | `/api/v1/vehiculos/{id}` | `VEHICULO_ESCRIBIR` | `200`, `400`, `401`, `403`, `404`, `409` |
| `DELETE`| `/api/v1/vehiculos/{id}` | `VEHICULO_ESCRIBIR` | `204`, `401`, `403`, `404`, `409` |
| `POST` | `/api/v1/ordenes` | `ORDEN_REGISTRAR` | `201`, `400`, `401`, `403`, `404`, `409` |
| `GET` | `/api/v1/ordenes` | `ORDEN_LEER` | `200`, `401`, `403` |
| `GET` | `/api/v1/ordenes/{id}` | `ORDEN_LEER` | `200`, `401`, `403`, `404` |
| `PATCH`| `/api/v1/ordenes/{id}/anular` | `ORDEN_ANULAR` | `200`, `400`, `401`, `403`, `404`, `409` |
| `GET` | `/api/v1/reportes/ingresos-por-servicio` | `REPORTE_VER` | `200`, `401`, `403` |

---

## 🏛️ Estructura de Paquetes
```
pe.edu.upeu.AutoAndes
├── config/       # Configuración de Seguridad, CORS, Swagger
├── controller/   # Controladores REST (/api/v1)
├── dto/          # Request y Response DTOs
├── entity/       # Entidades JPA (Oracle)
├── exception/    # GlobalExceptionHandler y Formato Común de Error
├── init/         # DataInitializer (Semilla con BCrypt)
├── mapper/       # Conversiones desacopladas Entidad <-> DTO
├── repository/   # Repositorios Spring Data JPA y JPQL
├── security/     # JwtService, Filtros, RBAC y Auth
└── service/      # Reglas de Negocio @Transactional
```
