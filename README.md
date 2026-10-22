# Taller 09: Comunicación Síncrona y Asíncrona de Microservicios

**Proyecto:** Banco de Preguntas Saber PRO — Universidad del Cauca, Ingeniería de Software II
**Autor:** Santiago Vidal Martínez

## ¿Qué hace este proyecto?

Implementa 6 microservicios independientes (cada uno un proyecto Spring Boot separado, con su propio `pom.xml` y su propio puerto) que colaboran mediante dos mecanismos de comunicación:

| Microservicio | Puerto | Qué hace | Comunicación |
|---|---|---|---|
| `user-service` | 8081 | Login de usuarios (docente, admin, revisor) | REST síncrono |
| `validation-service` | 8082 | Valida que una pregunta tenga 4 opciones y 1 respuesta correcta | REST síncrono |
| `question-service` | 8083 | Crea preguntas (llama a Validation) y solicita enviarlas a revisión | REST síncrono + publica en RabbitMQ |
| `lifecycle-service` | 8084 | Única autoridad del estado de la pregunta (DRAFT → PENDING_REVIEW → IN_REVIEW → APPROVED/REJECTED) | Escucha RabbitMQ + REST |
| `review-service` | 8085 | Asigna revisores y registra la decisión (aprobar/rechazar) | REST síncrono + publica en RabbitMQ |
| `notification-service` | 8086 | "Envía" (simula) una notificación cuando se asigna un revisor | Escucha RabbitMQ |

**Flujo completo:**
1. El docente hace login (`user-service`, síncrono).
2. Crea una pregunta (`question-service` llama síncronamente a `validation-service`; si es válida queda en `DRAFT`).
3. Pide enviarla a revisión → `question-service` **no espera**: publica un mensaje en RabbitMQ y responde `202 Accepted` de inmediato.
4. `lifecycle-service`, en segundo plano, recibe ese mensaje y cambia el estado a `PENDING_REVIEW`.
5. Un admin asigna un revisor (`review-service`, síncrono con `lifecycle-service` para pasar a `IN_REVIEW`).
6. Si la asignación fue exitosa, `review-service` publica un evento en RabbitMQ.
7. `notification-service` lo recibe en segundo plano y "envía" la notificación al revisor, sin bloquear nada.

Todo el estado se guarda en memoria (no hay base de datos real) — es suficiente para el alcance de este taller y cumple "Database per Service" porque cada microservicio tiene su propio repositorio aislado, nadie comparte tablas.

## Lo que NECESITAS para correrlo (importante)

A diferencia de los talleres anteriores (que podías compilar con `javac` sin internet), **este taller sí necesita**:
1. **Internet** (para que Maven descargue las dependencias de Spring Boot y RabbitMQ la primera vez).
2. **Docker Desktop instalado y corriendo** (para levantar RabbitMQ).

Si tu PC del laboratorio no tiene internet ni Docker, **no vas a poder levantar esto ahí**. Tendrías que hacerlo desde tu casa, o buscar un equipo de la universidad con internet y Docker instalado.

## Cómo correrlo paso a paso

### 1. Levantar RabbitMQ con Docker

Desde la raíz del proyecto (`lisw2_taller09_microservicios_g1/`):

```bash
docker compose up -d
```

Verifica que quedó arriba entrando a **http://localhost:15672** (usuario `guest`, contraseña `guest`). Ahí vas a ver las colas y poder tomar las capturas de pantalla que pide el informe.

### 2. Compilar y correr cada microservicio

Necesitas **6 terminales abiertas al mismo tiempo** (uno por microservicio), o usar la pestaña de ejecución de tu IDE para cada uno. En cada terminal, entra a la carpeta del servicio y corre:

```bash
cd user-service
mvn spring-boot:run
```

Repite para `validation-service`, `question-service`, `lifecycle-service`, `review-service` y `notification-service` (cada uno en su propia terminal, cada uno queda corriendo en su puerto).

**Orden recomendado de arranque** (no es obligatorio porque RabbitMQ guarda los mensajes en cola hasta que alguien los consuma, pero así ves los logs más ordenados): `lifecycle-service` y `notification-service` primero (son los que escuchan colas), luego los demás.

### 3. Probar con Postman

1. Abre Postman → Import → selecciona `postman/SaberPro_Microservices.postman_collection.json`.
2. Las variables de entorno (`{{user_service_url}}`, etc.) ya vienen configuradas en la colección apuntando a `localhost`.
3. Corre las carpetas en orden: `01 - User Service` → `02 - Validation Service` → `03 - Question Service` → `04 - Lifecycle Service` → `05 - Review Service` → `06 - Notification Service`. Las variables `authToken`, `questionId`, `operationId` y `reviewId` se van guardando automáticamente entre peticiones gracias a los scripts de test.
4. Cada petición ya trae un test automático (pestaña "Test Results" en Postman) que verifica el código de estado HTTP esperado.

### 4. Evidencias para el informe

- Captura del panel de RabbitMQ (`http://localhost:15672` → pestaña **Queues**) mostrando las colas `question.submit.review.queue` y `reviewer.assigned.queue` con mensajes entregados.
- Captura de Postman con la colección corrida (ventana "Collection Runner" o cada petición individual con su resultado en verde).
- Logs de consola de `lifecycle-service` y `notification-service` mostrando los `System.out.println(...)` que imprimen cuando reciben un mensaje.

### 5. Detener todo

```bash
docker compose down
```
Y cierra las 6 terminales de los microservicios (Ctrl+C en cada una).

## Estructura del proyecto

```
lisw2_taller09_microservicios_g1/
├── docker-compose.yml          # RabbitMQ (AMQP 5672 + Management 15672)
├── postman/
│   └── SaberPro_Microservices.postman_collection.json
├── user-service/                (:8081)
├── validation-service/          (:8082)
├── question-service/            (:8083)
├── lifecycle-service/           (:8084)
├── review-service/              (:8085)
└── notification-service/        (:8086)
```

## Usuarios de prueba (User Service)

| Usuario | Contraseña | Rol |
|---|---|---|
| `docente1` | `Docente123!` | DOCENTE, AUTOR |
| `admin1` | `Admin123!` | ADMIN |
| `revisor1` | `Revisor123!` | REVISOR |

## Nota sobre el "token"

El enunciado indica que **en esta versión del taller no se implementan tokens de acceso ni JWT reales** (se hará en el corte 3). Por eso `user-service` devuelve un identificador simple (no es un JWT válido) y `question-service` solo exige que el encabezado `Authorization` venga presente, sin validarlo criptográficamente. Esto es intencional y está documentado en el informe.
