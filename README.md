# SIGEP II — Proyecto Full Stack

Sistema web para la gestión de usuarios y hoja de vida de servidores públicos. El proyecto está dividido en dos aplicaciones:

- **Backend:** API REST construida con Spring Boot.
- **Frontend:** aplicación web construida con React, TypeScript y Vite.

---

## 1. Tecnologías principales

### Backend

- Java 21
- Spring Boot 3.2.0
- Spring Security
- JWT
- MongoDB
- Maven Wrapper
- Lombok
- Bean Validation
- Soporte de carga de archivos PDF e imágenes

### Frontend

- React 19
- TypeScript
- Vite
- React Router
- Axios
- CSS propio

---

## 2. Estructura general del proyecto

```txt
SIGEP-II-api-main/
├── backend/
│   ├── pom.xml
│   ├── mvnw
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/apirest/backend/
│       │   ├── config/
│       │   ├── controllers/
│       │   ├── dtos/
│       │   ├── exceptions/
│       │   ├── jwts/
│       │   ├── models/
│       │   ├── repositories/
│       │   └── services/
│       └── resources/
│           └── application.properties
│
└── frontend/
    ├── package.json
    ├── vite.config.ts
    ├── .env
    └── src/
        ├── components/
        ├── context/
        ├── hooks/
        ├── pages/
        ├── services/
        ├── styles/
        └── types/
```

---

## 3. Módulos funcionales

### Autenticación y usuarios

Permite:

- Iniciar sesión.
- Crear usuarios.
- Inhabilitar usuarios.
- Cambiar contraseña.
- Solicitar enlace de recuperación.
- Recuperar contraseña desde token.

### Hoja de vida / Curriculum

Está dividido en:

1. **Datos personales**
   - Datos básicos.
   - Datos demográficos.
   - Datos de contacto.

2. **Educación**
   - Formación académica.
   - Educación para el trabajo.
   - Idiomas.

3. **Experiencia laboral**
   - Experiencia laboral general.
   - Experiencia laboral docente.

4. **Gerencia pública**
   - Publicaciones.
   - Premios y reconocimientos.
   - Participación en proyectos.
   - Participación en corporaciones o entidades.

5. **Archivos**
   - Carga de PDF, JPG, JPEG y PNG.
   - Consulta de archivos cargados.

---

## 4. Requisitos previos

Instala antes de ejecutar el proyecto:

- Java 21.
- Node.js 20 o superior.
- npm.
- Acceso a una base de datos MongoDB.
- Git, opcional pero recomendado.

Verifica versiones:

```bash
java -version
node -v
npm -v
```

---

## 5. Configuración del backend

Archivo principal:

```txt
backend/src/main/resources/application.properties
```

Variables importantes:

```properties
spring.application.name=backend

spring.data.mongodb.uri=${SPRING_MONGODB_URI:mongodb://localhost:27017/BD-SIGED-II}
spring.data.mongodb.database=${SPRING_MONGODB_DATABASE:BD-SIGED-II}

jwt.llave=CAMBIAR_POR_UN_SECRETO_SEGURO
jwt.expiracion=3600000

resend.api.key=CAMBIAR_POR_API_KEY_REAL

app.upload-dir=${APP_UPLOAD_DIR:uploads}
app.upload.max-size-bytes=${APP_UPLOAD_MAX_SIZE_BYTES:10485760}
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Recomendación importante

No guardes credenciales reales directamente en el repositorio. Usa variables de entorno para:

```txt
SPRING_MONGODB_URI
SPRING_MONGODB_DATABASE
JWT_SECRET
RESEND_API_KEY
APP_UPLOAD_DIR
APP_UPLOAD_MAX_SIZE_BYTES
```

Si decides leer `JWT_SECRET` y `RESEND_API_KEY` desde variables de entorno, ajusta `application.properties` así:

```properties
jwt.llave=${JWT_SECRET}
resend.api.key=${RESEND_API_KEY}
```

---

## 6. Ejecutar backend en local

Desde la raíz del proyecto:

```bash
cd backend
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

Cuando levante correctamente, la API queda disponible en:

```txt
http://localhost:8080
```

---

## 7. Configuración del frontend

Archivo:

```txt
frontend/.env
```

Contenido para ambiente local:

```env
VITE_API_URL=http://localhost:8080
```

El frontend usa esta variable en:

```txt
frontend/src/services/api.ts
```

La URL del backend no debe quedar escrita directamente en el código. Debe venir desde `VITE_API_URL`.

---

## 8. Ejecutar frontend en local

Desde la raíz del proyecto:

```bash
cd frontend
npm install
npm run dev
```

La aplicación web queda disponible normalmente en:

```txt
http://localhost:5173
```

---

## 9. Flujo básico de uso

### 1. Iniciar sesión

El usuario ingresa con tipo de identificación, número de identificación y contraseña.

El backend responde con un JWT. El frontend guarda la sesión en `localStorage`.

### 2. Acceder al panel principal

Después del login, el usuario entra al dashboard.

### 3. Completar datos personales

El orden correcto es:

```txt
Datos básicos
↓
Datos demográficos
↓
Datos de contacto
```

Los datos básicos son obligatorios porque crean el curriculum inicial del usuario.

### 4. Completar educación

El usuario puede agregar varios registros de:

- Formación académica.
- Educación para el trabajo.
- Idiomas.

Cada registro nuevo se crea con `POST`. Si ya existe un registro y tiene `id`, se actualiza con `PUT`.

### 5. Completar experiencia laboral

El usuario puede agregar:

- Experiencia laboral general.
- Experiencia docente.

Cada registro nuevo se crea con `POST`. Los registros existentes se actualizan usando su identificador.

### 6. Completar gerencia pública

Esta sección permite registrar y consultar información de gerencia pública. Actualmente el backend permite crear y consultar registros.

---

## 10. Autenticación y roles

El backend usa JWT con Spring Security.

### Rutas públicas

No requieren token:

```txt
POST /api/auth/login
POST /api/auth/pedirEnlace
POST /api/auth/recuperarContraseña
```

### Rutas protegidas

El resto de rutas requiere token:

```http
Authorization: Bearer TOKEN_JWT
```

### Roles principales

```txt
servidorPublico
jefeDeTalentoHumano
```

El rol `jefeDeTalentoHumano` puede crear e inhabilitar usuarios.

---

## 11. OpenAPI

El proyecto está organizado como API REST y se puede documentar con OpenAPI.

### Estado actual

El backend ya tiene controladores REST claros, pero el `pom.xml` actual no incluye todavía la dependencia de documentación OpenAPI/Swagger UI.

### Cómo habilitar OpenAPI en Spring Boot

Agrega al archivo:

```txt
backend/pom.xml
```

esta dependencia dentro de `<dependencies>`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.17</version>
</dependency>
```

Luego ejecuta de nuevo el backend.

### URLs de documentación OpenAPI

Con la dependencia anterior, normalmente tendrás:

```txt
http://localhost:8080/v3/api-docs
http://localhost:8080/swagger-ui/index.html
```

### Configuración recomendada para permitir Swagger UI

En `SecurityConfig.java`, agrega estas rutas como públicas:

```java
.requestMatchers(
    "/api/auth/login",
    "/api/auth/pedirEnlace",
    "/api/auth/recuperarContraseña",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html"
).permitAll()
```

### Seguridad OpenAPI esperada

La API usa Bearer Token:

```yaml
components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

Para probar endpoints protegidos desde Swagger UI, inicia sesión primero en `/api/auth/login`, copia el token y úsalo en el botón **Authorize**.

---

## 12. Referencia rápida de API

Base URL local:

```txt
http://localhost:8080
```

### Auth

| Método | Ruta | Descripción | Token |
|---|---|---|---|
| POST | `/api/auth/login` | Inicia sesión | No |
| POST | `/api/auth/registro` | Crea usuario | Sí |
| PUT | `/api/auth/inhabilitarUsuario` | Inhabilita usuario | Sí |
| PUT | `/api/auth/cambiarContraseña` | Cambia contraseña del usuario autenticado | Sí |
| POST | `/api/auth/pedirEnlace` | Solicita enlace de recuperación | No |
| POST | `/api/auth/recuperarContraseña?token=...` | Recupera contraseña | No |

### Archivos

| Método | Ruta | Descripción | Token |
|---|---|---|---|
| POST | `/api/archivos` | Carga archivo PDF o imagen | Sí |
| GET | `/api/archivos/{nombreArchivo}` | Consulta archivo cargado | Sí |

### Datos personales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/curriculum/datosPersonales/datosBasicos` | Registra datos básicos |
| PUT | `/api/curriculum/datosPersonales/datosBasicos` | Actualiza datos básicos |
| GET | `/api/curriculum/datosPersonales/datosBasicos` | Obtiene datos básicos |
| POST | `/api/curriculum/datosPersonales/datosDemograficos` | Registra datos demográficos |
| PUT | `/api/curriculum/datosPersonales/datosDemograficos` | Actualiza datos demográficos |
| GET | `/api/curriculum/datosPersonales/datosDemograficos` | Obtiene datos demográficos |
| POST | `/api/curriculum/datosPersonales/datosContacto` | Registra datos de contacto |
| PUT | `/api/curriculum/datosPersonales/datosContacto` | Actualiza datos de contacto |
| GET | `/api/curriculum/datosPersonales/datosContacto` | Obtiene datos de contacto |

### Educación

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/curriculum/educacion/formacionAcademica` | Registra formación académica |
| PUT | `/api/curriculum/educacion/formacionAcademica` | Actualiza formación académica |
| GET | `/api/curriculum/educacion/formacionAcademica` | Lista formaciones académicas |
| GET | `/api/curriculum/educacion/formacionAcademica/{formacionId}` | Consulta una formación académica |
| POST | `/api/curriculum/educacion/trabajo` | Registra educación para el trabajo |
| PUT | `/api/curriculum/educacion/trabajo` | Actualiza educación para el trabajo |
| GET | `/api/curriculum/educacion/trabajo` | Lista educación para el trabajo |
| GET | `/api/curriculum/educacion/trabajo/{educacionId}` | Consulta una educación para el trabajo |
| POST | `/api/curriculum/educacion/idioma` | Registra idioma |
| PUT | `/api/curriculum/educacion/idioma` | Actualiza idioma |
| GET | `/api/curriculum/educacion/idioma` | Lista idiomas |
| GET | `/api/curriculum/educacion/idioma/{idiomaId}` | Consulta un idioma |

### Experiencia laboral

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/curriculum/experienciaLaboral` | Registra experiencia laboral |
| PUT | `/api/curriculum/experienciaLaboral` | Actualiza experiencia laboral |
| GET | `/api/curriculum/experienciaLaboral` | Lista experiencias laborales |
| GET | `/api/curriculum/experienciaLaboral/{experienciaLaboralId}` | Consulta una experiencia laboral |
| POST | `/api/curriculum/experienciaLaboral/docente` | Registra experiencia docente |
| PUT | `/api/curriculum/experienciaLaboral/docente` | Actualiza experiencia docente |
| GET | `/api/curriculum/experienciaLaboral/docente` | Lista experiencias docentes |
| GET | `/api/curriculum/experienciaLaboral/docente/{experienciaLaboralId}` | Consulta una experiencia docente |

### Gerencia pública

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/curriculum/gerenciaPublica/publicacion` | Registra publicación |
| GET | `/api/curriculum/gerenciaPublica/publicacion` | Lista publicaciones |
| GET | `/api/curriculum/gerenciaPublica/publicacion/{publicacionId}` | Consulta publicación |
| POST | `/api/curriculum/gerenciaPublica/premioReconocimiento` | Registra premio o reconocimiento |
| GET | `/api/curriculum/gerenciaPublica/premioReconocimiento` | Lista premios o reconocimientos |
| GET | `/api/curriculum/gerenciaPublica/premioReconocimiento/{premioId}` | Consulta premio o reconocimiento |
| POST | `/api/curriculum/gerenciaPublica/participacionProyecto` | Registra participación en proyecto |
| GET | `/api/curriculum/gerenciaPublica/participacionProyecto` | Lista participaciones en proyectos |
| GET | `/api/curriculum/gerenciaPublica/participacionProyecto/{participacionId}` | Consulta participación en proyecto |
| POST | `/api/curriculum/gerenciaPublica/participacionCorporacionEntidad` | Registra participación en corporación o entidad |
| GET | `/api/curriculum/gerenciaPublica/participacionCorporacionEntidad` | Lista participaciones en corporaciones o entidades |
| GET | `/api/curriculum/gerenciaPublica/participacionCorporacionEntidad/{corporacionId}` | Consulta participación en corporación o entidad |

---

## 13. Ejemplos de uso con cURL

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "tipoIdentificacion": "CedulaDeCiudadania",
    "numeroIdentificacion": "123456789",
    "contraseña": "Clave123"
  }'
```

Respuesta esperada:

```json
{
  "tipoIdentificacion": "CedulaDeCiudadania",
  "numeroIdentificacion": "123456789",
  "token": "TOKEN_JWT"
}
```

### Crear datos básicos

```bash
curl -X POST http://localhost:8080/api/curriculum/datosPersonales/datosBasicos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_JWT" \
  -d '{
    "nombre": "Juan Pérez",
    "tipoIdentificacion": "CedulaDeCiudadania",
    "numeroIdentificacion": "123456789",
    "fechaNacimiento": "1990-01-01T00:00:00.000Z",
    "email": "juan@example.com",
    "genero": "MASCULINO",
    "personaExpuestaPoliticamente": false
  }'
```

### Subir archivo

```bash
curl -X POST http://localhost:8080/api/archivos \
  -H "Authorization: Bearer TOKEN_JWT" \
  -F "archivo=@documento.pdf"
```

Respuesta esperada:

```json
{
  "nombreArchivo": "uuid.pdf",
  "url": "/api/archivos/uuid.pdf",
  "tipoContenido": "application/pdf",
  "tamañoBytes": 123456
}
```

Luego se guarda la `url` devuelta en el campo correspondiente del curriculum.

---

## 14. Carga de archivos

El backend permite cargar documentos en:

```txt
POST /api/archivos
```

Formatos permitidos:

```txt
.pdf
.jpg
.jpeg
.png
```

Tamaño máximo por defecto:

```txt
10 MB
```

La carpeta de almacenamiento se configura con:

```properties
app.upload-dir=${APP_UPLOAD_DIR:uploads}
```

El flujo recomendado es:

```txt
1. El usuario selecciona un archivo.
2. El frontend sube el archivo a /api/archivos.
3. El backend devuelve una URL.
4. El frontend guarda esa URL en el formulario correspondiente.
5. El usuario puede ver el documento usando esa URL.
```

Campos que pueden guardar URLs de documentos:

```txt
documentoIdentificacion
libretaMilitar
archivoTarjetaProfesional
archivoEducacionFormal
diplomaActaCertificadoEstudio
certificado
certificadoLaboral
```

---

## 15. Formato de fechas

El backend usa `Instant`, por eso las fechas deben enviarse en formato ISO 8601:

```txt
2026-05-20T00:00:00.000Z
```

No enviar fechas como:

```txt
20/05/2026
2026-05-20
```

En el frontend se recomienda convertir las fechas antes de enviarlas:

```ts
new Date(`${date}T00:00:00.000Z`).toISOString()
```

---

## 16. Reglas importantes para el frontend

### Datos personales

Antes de guardar datos demográficos o de contacto, deben existir datos básicos.

### Educación

Para agregar varios registros:

- Un registro sin `id` se crea con `POST`.
- Un registro con `id` se actualiza con `PUT`.

### Experiencia laboral

La experiencia laboral y la experiencia docente se manejan como listas.

Para actualizar un registro existente, el frontend debe enviar su identificador:

```txt
experienciaLaboralId
experienciaLaboralDocenteId
```

### Gerencia pública

El backend permite crear y consultar registros. Si se necesita edición o eliminación, hay que agregar endpoints `PUT` y `DELETE` en backend.

---

## 17. Enums importantes

Los valores enviados desde el frontend deben coincidir exactamente con los enums del backend.

### Roles

```txt
servidorPublico
jefeDeTalentoHumano
```

### Tipo de identificación

```txt
CedulaDeCiudadania
CedulaDeExtranjeria
Pasaporte
TarjetaDeIdentidad
PermisoDeProteccionTemporal
```

El backend contiene más valores disponibles en:

```txt
backend/src/main/java/com/apirest/backend/models/enums/Usuario/TipoIdentificacionUsuarios.java
```

### Género

```txt
MASCULINO
FEMENINO
```

### Estado civil

```txt
CASADO
DIVORCIADO
SEPARADO
SOLTERO
UNIONLIBRE
VIUDO
```

### Zona

```txt
URBANA
RURAL
```

### Clase libreta militar

```txt
PRIMERA_CLASE
SEGUNDA_CLASE
PROVISIONAL
```

### Estado de estudio

```txt
En_proceso
Finalizado
```

### Nivel de idioma

```txt
BIEN
MUY_BIEN
NINGUNO
REGULAR
```

### Jornada laboral

```txt
MEDIO_TIEMPO
TIEMPO_COMPLETO
TIEMPO_PARCIAL
```

### Tipo de entidad

```txt
PUBLICA
PRIVADA
PRIVADA_CON_FUNCIONES_PUBLICAS
```

---

## 18. CORS

El backend permite llamadas desde orígenes definidos en:

```txt
backend/src/main/java/com/apirest/backend/config/SecurityConfig.java
```

Configuración actual esperada:

```java
configuration.setAllowedOrigins(List.of(
    "http://localhost:5173"
));
```

Si el frontend corre en otro puerto o dominio local, agrégalo a esta lista.

---

## 19. Errores comunes

### Error 401 o 403

Causas probables:

- No se envió token.
- El token expiró.
- El usuario no tiene el rol requerido.

Solución:

- Iniciar sesión otra vez.
- Revisar el encabezado `Authorization`.

### Error 409 al guardar contacto, educación o experiencia

Causa probable:

- El usuario todavía no tiene datos básicos guardados.

Solución:

- Guardar primero datos básicos.

### Error por formato inválido

Causas probables:

- Enum enviado con valor incorrecto.
- Fecha enviada sin formato `Instant`.
- Campo obligatorio vacío.

Solución:

- Revisar DTOs en backend.
- Revisar enums en backend.
- Confirmar que el frontend envíe valores exactos.

### No se puede ver un archivo

Causas probables:

- El archivo no existe en la carpeta `uploads`.
- El nombre guardado no coincide.
- No se envió token.

Solución:

- Confirmar que la URL guardada empiece con `/api/archivos/`.
- Confirmar que el archivo existe en la carpeta configurada.

---

## 20. Comandos útiles

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

### Compilar frontend

```bash
cd frontend
npm run build
```

---

## 21. Archivos principales para revisar

### Backend

```txt
backend/src/main/java/com/apirest/backend/controllers/AuthController.java
backend/src/main/java/com/apirest/backend/controllers/CurriculumController.java
backend/src/main/java/com/apirest/backend/controllers/ArchivoController.java
backend/src/main/java/com/apirest/backend/services/AuthServiceImp.java
backend/src/main/java/com/apirest/backend/services/CurriculumServiceImp.java
backend/src/main/java/com/apirest/backend/services/FileStorageService.java
backend/src/main/java/com/apirest/backend/config/SecurityConfig.java
backend/src/main/resources/application.properties
```

### Frontend

```txt
frontend/src/services/api.ts
frontend/src/types/index.ts
frontend/src/context/AuthContext.tsx
frontend/src/hooks/useAuth.ts
frontend/src/components/ProtectedRoute.tsx
frontend/src/components/common/FileUploadField.tsx
frontend/src/pages/dashboard/DashboardPage.tsx
frontend/src/pages/curriculum/DatosPersonalesPage.tsx
frontend/src/pages/curriculum/EducacionPage.tsx
frontend/src/pages/curriculum/ExperienciaPage.tsx
frontend/src/pages/curriculum/GerenciaPublicaPage.tsx
```

---

## 22. Resumen rápido para levantar el proyecto

Terminal 1:

```bash
cd backend
./mvnw spring-boot:run
```

Terminal 2:

```bash
cd frontend
npm install
npm run dev
```

Abrir:

```txt
http://localhost:5173
```

API local:

```txt
http://localhost:8080
```

OpenAPI, si se habilita con Springdoc:

```txt
http://localhost:8080/swagger-ui/index.html
```
