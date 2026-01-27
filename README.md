# StayHub - Reservation 

## 📍 Estado del proyecto

🚧 Proyecto en desarrollo

La arquitectura y los componentes evolucionan de forma progresiva conforme se incorporan nuevas funcionalidades y tecnologías.
Para consultar el código del proyecto se debe acceder al branch "testing"

## 📌 Descripción

StayHub - Reservation es el microservicio encargado de gestionar todo el ciclo de vida de las reservas y bloqueos de habitaciones dentro del proyecto StayHub

Este servicio forma parte de la arquitectura basada en microservicios orientada al aprendizaje y el diseño de sistemas backend actuales, simulando el funcionamiento real de una plataforma de gestión de alojamientos y reservas.

El objetivo principal de este microservicio es centralizar la lógica relacionada con: 

- Creación de reservas
- Cancelación de reservas
- Gestión de estados de reserva
- Consulta de reservas por usuario
- Consulta de reservas por propietario
- Gestión de bloqueos de habitaciones
- Validaciones de disponibilidad reales

## 🎯 Responsabilidad del microservicio

- Este microservicio cumple con las siguientes responsabilidades:
- Crear reservas de habitaciones
- Calcular el precio total de la reserva
- Asignar propietario y usuario implicados
- Gestionar el estado de las reservas
- Permitir la cancelación de reservas
- Mantener el historial completo de reservas
- Crear y eliminar bloqueos de habitaciones
- Validar solapamientos entre reservas y bloqueos
- Proveer la verdad final sobre disponibilidad

Este microservicio es el encargado de aportar la información real sobre ocupación

Este microservicio no es responsable de: 
- Crear alojamientos
- Crear habitaciones
- Realizar búsquedas de alojamientos
- Publicar alojamientos
- Getionar países o ciudades

## 🧩 Modelo de dominio

**Reserva**

Una reserva es representada por la intención de un usuario de ocupar una habitación durante un rango de fechas determinado

Cada reserva contiene: 
- Identificador único de la reserva
- Identificador único de la habitación
- Identificador único del usuario
- Identificador único del propietario
- Fecha de entrada
- Fecha de salida
- Fecha de creación
- Estado de la reserva
- Precio total
- Tipo de habitación

Las reservas no se eliminan nunca, solo cambian de estado

Los estados de las reservas son los siguientes:
- Pendiente
- Confirmada
- Cancelado
- Completada

Este diseño permite mantener histórico de reservas

**Bloqueos**

Los bloqueos son representados como restricciones realizadas por el propietario sobre una habitación

Características principales:
- Solo el propietario puede crear bloqueos
- Los bloqueos tienen un rango de fechas
- Un bloqueo impide nuevas reservas o bloqueos
- No puede solaparse con reservas o bloqueos ya existentes

A diferencia de las reservas, un bloqueo si podrá ser eliminado de manera definitiva, ya que se comprenden como una restricción temporal

Este sistema permite: 
- Cerrar habitaciones temporalmente
- Retirar alojamientos de las búsquedas
- Evitar la eliminación de datos históricos

## 🔄 Flujo general de reservas

**1️⃣ Búsqueda de disponibilidad**

El proceso de busqueda se realiza desde el microservicio StayHub-Accommodation, el cual solicita al microservicio StayHub-Reservation la verificación de disponibilidad

Reservation valida:
- Reservas existentes
- Bloqueos activos
- Solapamientos de fechas

Solo las habitaciones disponibles continúan el proceso

**2️⃣ Creación de reserva**

Para crear una reserva es obligatorio:
- Estar autenticado como usuario
- Proporcionar la habitación que quiere ser reservada

El sistema obtendrá los datos automáticamente debido a que previamente han sido guardados y actualizados en todo momento por StayHub-Accommodation, estos datos son enviados mediante comunicación gRPC

**3️⃣ Consulta de reservas del usuario**

Un usuario puede consultar todas sus reservas:
- Activas
- Completadas
- Canceladas
- Pendientes de confirmación

**4️⃣ Cancelación de reserva**

Una reserva puede ser cancelada por el usuario

La cancelación no elimina la reserva, únicamente cambia su estado a cancelado

**5️⃣ Reservas del propietario**

El propietario puede consultar todas las reservas realizadas sobre sus habitaciones, independientemente del estado de las mismas

## 🧱 Sistema de bloqueos

El propietario puede: 
- Crear bloqueos por rango de fechas
- Consultar bloqueos existentes
- Eliminar bloqueos

Los bloqueos: 
- Impiden nuevas reservas
- No afectan a reservas ya creadas
- Pueden ser eliminadas

Este sistema pemite:
- Cierre temporal de habitaciones (o alojamiento en el caso de que todas las habitaciones esten bloqueadas)
- Deshabilitación de un alojamiento sin eliminar la información

## 🔗 Comunicación entre microservicios

Este microservicio se comunica mediante gRPC con: 

StayHub - Accommodation

Utilizado para consultas realizadas por parte de StayHub - Accommodation: 

 - Verificar disponibilidad
 - Validar solapamientos
 - Impedir eliminación de habitaciones con reservas futuras

## 🔐 Seguridad y control de acceso

- Los usuarios solo pueden gestionar sus propias reservas
- Los propietarios solo pueden gestionar reservas de sus habitaciones
- Los bloqueos solo pueden ser creados por propietarios
- Todos los endpoint están protegidos mediante JWT
- El token determina el contexto de usuario o propietario

## 🛠️ Tecnologías utilizadas
- Java
- Spring Boot
- Spring Security
- JWT
- JPA / Hibernate
- gRPC
- REST API
- JSON
- H2 (para desarrollo)
- Git

## 📘 Contexto del proyecto

Este microservicio forma parte del proyecto StayHub, una proyecto backend diseñado con fines de aprendizaje y de arquitectura, orientada a simular escenarios reales utilizados en sistemas de gestión de alojamientos.
