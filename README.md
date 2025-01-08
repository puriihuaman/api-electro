# API ELECTRO

API Electro es una API REST desarrollada en Java utilizando el framework Spring Boot. Esta API permite a los usuarios
gestionar productos, categorías y usuarios mediante las operaciones básicas de CRUD (Crear, Leer, Actualizar y
Eliminar). Además, implementa seguridad con Spring Security y JWT para la autenticación y autorización de usuarios, así
como manejo avanzado de errores para garantizar una experiencia de usuario robusta.

![Demo](/src/main/resources/static/demo/demo.png)

## Características principales

- **Gestión de usuarios:** CRUD para usuarios con roles diferenciados (admin, user, invited).
- **Gestión de productos:** Permite crear, leer, actualizar y eliminar productos.
- **Gestión de categorías:** CRUD completo para la administración de categorías.
- **Autenticación y autorización:**
    - Autenticación basada en JSON Web Tokens (JWT).
    - Autorización basada en roles con control de acceso a endpoints específicos.
- **Arquitectura basada en capas:** Separación de responsabilidades en controladores, servicios, repositorios y
  entidades.
- **Manejo avanzado de excepciones:** Gestión centralizada de errores con respuestas claras y específicas.

## Tecnologías utilizadas

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3
- **Base de datos:** MySQL
- **Seguridad:** Spring Security y JWT
- **Dependencias adicionales:**
    - Spring Data JPA
    - Hibernate
    - Lombok
    - Maven
    - Hateoas
    - Mapstruct

## Instalación y configuración

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/puriihuaman/api-electro.git
   cd api-electro
   ```
2. **Configurar la base de datos:**
    - Crear una base de datos en MySQL.
        - Buscar el fichero del script de la base de datos en: `resources/static/db/electro.sql`.
        - Copiar el contenido o abrirlo desde `Worbench`.
        - Luego ejecutar todo el script.
    - Actualizar el archivo `application.properties` con las credenciales de tu base de datos:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/nombre_base_datos
      spring.datasource.username=tu_usuario
      spring.datasource.password=tu_contraseña
      ```
3. **Configurar propiedades para JWT:**
   ```properties
   jwt.secret.key=tu_clave_secreta
   jwt.time.expiration=tu_tiempo_de_expiración_para_tu_token
   ```
4. **Verificar la clase principal `ApiElectroApplication.java`**
    - Aquí en esta clase se va a crear el primer usuario al ejecutar
    - Las credenciales que se usarían para hacer login:
        - username: `ANON2025`
        - password: `AdM¡N&20_25`
    - `NOTA`: El `username` es autogenerado mediante un trigger cuando se crea el usuario y la contraseña se encripta.
5. **Construir el proyecto:**
   ```bash
   mvn clean install
   ```

6**Ejecutar la aplicación:**

   ```bash
   mvn spring-boot:run
   ```

7**Importa la colección en tu Rest Client**
- Buscar la carpeta de los endpoints en: `resources/static/collection_api/API Electro`
- Importa la carpeta en tu **Rest Client**
favorito ([Postman](https://www.postman.com/), [Bruno](https://www.usebruno.com/) o otro).

## Endpoints principales

### Autenticación

- **Login:** `POST /api/auth/login`
    - Solicita: `{ "username": "user", "password": "password" }`
    - Responde: JWT Token

### Usuarios

- **Crear usuario:** `POST /api/users`
- **Consultar usuarios:** `GET /api/users`
- **Actualizar usuario:** `PUT /api/users/{id}`
- **Eliminar usuario:** `DELETE /api/users/{id}`

### Productos

- **Crear producto:** `POST /api/products`
- **Consultar productos:** `GET /api/products`
- **Actualizar producto:** `PUT /api/products/{id}`
- **Eliminar producto:** `DELETE /api/products/{id}`

### Categorías

- **Crear categoría:** `POST /api/categories`
- **Consultar categorías:** `GET /api/categories`
- **Actualizar categoría:** `PUT /api/categories/{id}`
- **Eliminar categoría:** `DELETE /api/categories/{id}`

## Seguridad

- Los endpoints están protegidos con JWT.
- Roles disponibles:
    - **Admin:** Acceso completo a todos los recursos.
    - **User:** Acceso limitado a ciertos recursos.
    - **Invited:** Permisos mínimos.

## Manejo de errores

La API implementa un manejo centralizado de excepciones para:

- Recursos no encontrados.
- Errores de validación.
- Acceso no autorizado.
- Errores internos del servidor.

## Contribuciones

Si deseas contribuir al proyecto, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama nueva para tu funcionalidad o mejora.
3. Envía un pull request con una descripción detallada de los cambios.



