# StayHub - Reservation 

## 📍 Estado del proyecto

✅ **Finalizado**

Este repositorio contiene el microservicio de **Reservas**. Es el motor central que posee la "verdad absoluta" sobre la disponibilidad dentro del ecosistema StayHub. Gestiona el ciclo de vida completo de las reservas y las restricciones de ocupación, garantizando que no existan colisiones de fechas.

---

## 🎯 Hitos del microservicio

### ⚙️ Motor de disponibilidad estricto
Se ha desarrollado un validador de ocupación que actúa como fuente de la verdad para todo el sistema:
* **Prevención de colisiones:** Comprueba en tiempo real solapamientos, bloqueos activos y reservas futuras (`CheckFutureReservation`).
* **Respuesta enriquecida:** El método `createReservation` no solo guarda la reserva, sino que calcula automáticamente el **Precio Total** y devuelve los datos completos del cliente y la propiedad, facilitando el trabajo del Frontend.

### 🔄 Ciclo de vida automatizado y trazabilidad
Implementación de lógica de negocio para la consistencia del histórico:
* **Inmutabilidad de reservas:** Las reservas nunca se eliminan, solo cambian de estado (Pendiente, Confirmada, Cancelada, Completada) para mantener una trazabilidad histórica perfecta.
* **Cron Jobs (Automatización):** Se han implementado tareas programadas en segundo plano. Las reservas se confirman automáticamente a los 5 minutos de su creación, y a las 11:00 AM del día de *check-out*, su estado pasa automáticamente a "Completada".

### 🚧 Sistema de bloqueos
Para otorgar control total a los propietarios sin corromper los datos:
* **Restricciones temporales:** Permite cerrar habitaciones por rangos de fechas con motivos específicos (mantenimiento, personal, temporada, otros).
* **Protección de datos:** El sistema impide crear un bloqueo si este pisa o solapa una reserva previamente confirmada. A diferencia de las reservas, los bloqueos sí admiten borrado físico al ser una restricción temporal.

### 🔐 Seguridad y aislamiento de datos
Se ha implementado `Spring Security` y filtrado JWT para un control de acceso basado en la propiedad de los datos:
* **Contexto de Usuario (`User`):** Tienen acceso restringido a `GetMyReservation` y `CancelReservation` (solo pueden ver y cancelar sus propias reservas). Queda registrado el UUID del cliente de forma automática.
* **Contexto de Propietario (`Owner`):** Mediante `GetOwnerReservation`, pueden obtener todas las reservas realizadas en sus alojamientos y gestionar los bloqueos de sus habitaciones.

### 🔗 Orquestación distribuida (gRPC)
Este microservicio interactúa constantemente de forma bidireccional mediante gRPC:
* **Con StayHub-Accommodation:** Recibe peticiones para verificar la disponibilidad real antes de mostrar resultados de búsqueda. Además, recibe el identificador y el nombre del alojamiento para guardarlos en el registro de la reserva.
* **Protección estructural:** Evita que el microservicio de alojamientos pueda eliminar habitaciones o alojamientos si estos contienen reservas futuras activas.

---

## 🛠️ Tecnologías utilizadas
* **Lenguaje y Framework:** Java 17, Spring Boot
* **Seguridad y Acceso:** Spring Security, JWT (JSON Web Tokens)
* **Persistencia y ORM:** JPA, Hibernate, PostgreSQL (Producción), H2 (Desarrollo y Testing)
* **Arquitectura y Comunicación:** Patrón Microservicios, gRPC, API REST, JSON
* **Manejo de Errores:** Interceptores y Excepciones personalizadas para evitar fallos de concurrencia.
* **DevOps y Despliegue:** Docker, Docker Compose (gestión de puertos para gRPC), Git, GitHub.

---

## 🚀 Próximos pasos en StayHub

StayHub se basa en una arquitectura diseñada en la separación de responsabilidades, lo que facilita futuras integraciones como:

* Implementación de pasarela de pagos integrada en la reserva.
* Sistema de notificaciones automáticas (Email/SMS) al cambiar el estado de la reserva.
* Panel de estadísticas y nivel de ocupación para propietarios.
