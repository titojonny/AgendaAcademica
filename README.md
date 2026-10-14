# Agenda Académica Móvil - Patrón DAO con Persistencia JSON

**Universidad Francisco Gavidia (UFG)**  
**Facultad de Ingeniería y Sistemas**  
**Asignatura:** Desarrollo de Aplicaciones Multiplataforma (DAP)  
**Repositorio GitHub:** [https://github.com/titojonny/AgendaAcademica](https://github.com/titojonny/AgendaAcademica)  
**Equipo de Desarrollo:**
- **Jonathan Adonay Alas Girón** (Rol A - Modelo y Contrato)
- **Mauro José Alfaro Amaya** (Rol B - Persistencia JSON y Pruebas)
- **Lenny Adrián Elías Sánchez** (Rol C - Interfaz Móvil y Vistas)
**Plataforma:** Android con Java (minSdk 24, compileSdk/targetSdk 34)

---

## 📌 1. Descripción del Proyecto

Aplicación móvil Android para la gestión de materias y tareas académicas de estudiantes, implementando el patrón de diseño **Data Access Object (DAO)** desacoplado con persistencia en archivos **JSON** ubicados en el almacenamiento interno del dispositivo (`/data/data/sv.edu.ufg.agenda/files/datos/`).

El sistema respeta una arquitectura multicapa estricta donde las actividades de la interfaz de usuario (`Activity`) interactúan exclusivamente a través de contratos (interfaces Java), sin conocer jamás si los datos provienen de archivos JSON, de memoria RAM o de una base de datos SQLite/Room.

---

## 🏛️ 2. Arquitectura y Patrones de Diseño

```text
Capa UI (Pantallas)                Contratos (Interfaces)             Implementaciones de Datos
-------------------                ----------------------             --------------------------
MateriasActivity --------+------>  MateriaDAO (extends DAO<Materia>) -> MateriaDAOJson (hereda de JsonDAOBase<T>)
                         |                                           -> MateriaDAOMemoria (Actividad 6)
                         |
TareasActivity   --------+------>  TareaDAO (extends DAO<Tarea>)     -> TareaDAOJson (hereda de JsonDAOBase<T>)
                         |                                           -> TareaDAOMemoria (Actividad 6)
                         |
                   DAOFactory (creado en AgendaApp)
```

### Principios Clave:
1. **Desacoplamiento Absoluto:** Ninguna Activity, Adapter ni clase de UI importa `MateriaDAOJson`, `TareaDAOJson` ni clases de entrada/salida (`java.io.*` o Gson).
2. **Reutilización Genérica (`JsonDAOBase<T>`):** Las operaciones comunes de lectura, escritura con Gson pretty-printing, serialización UTF-8 y autoincremento de IDs se programan una sola vez.
3. **Fábrica Centralizada (`DAOFactory`):** Único punto del sistema donde se decide qué implementación concreta de DAO se entrega a las pantallas.
4. **Ciclo de Vida Limpio (`AgendaApp`):** Instanciada en la clase `Application` de Android para mantener viva la fábrica y evitar recreaciones innecesarias.

---

## 📂 3. Estructura del Código Fuente

```text
AgendaAcademica/
├── app/
│   ├── build.gradle
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/sv/edu/ufg/agenda/
│       │   │   ├── AgendaApp.java          # Configura e inicializa DAOFactory en getFilesDir()/datos
│       │   │   ├── modelo/                 # Paquete de modelos y contratos de entidad
│       │   │   │   ├── Entidad.java        # Interfaz base con getId() y setId(int id)
│       │   │   │   ├── Materia.java        # Atributos: id, nombre, docente
│       │   │   │   └── Tarea.java          # Atributos: id, materiaId, titulo, fechaEntrega, completada, prioridad
│       │   │   ├── dao/                    # Paquete de acceso a datos y contratos
│       │   │   │   ├── DAO.java            # Contrato genérico con CRUD básico
│       │   │   │   ├── MateriaDAO.java     # Contrato extendido con buscarPorNombre(String)
│       │   │   │   ├── TareaDAO.java       # Contrato con listarPorMateria(int) y buscarPorTitulo(...)
│       │   │   │   ├── JsonDAOBase.java    # Clase base abstracta genérica para persistencia JSON
│       │   │   │   ├── MateriaDAOJson.java # Persistencia JSON para materias
│       │   │   │   ├── TareaDAOJson.java   # Persistencia JSON para tareas
│       │   │   │   ├── MateriaDAOMemoria.java # Implementación alternativa en memoria (Actividad 6)
│       │   │   │   ├── TareaDAOMemoria.java   # Implementación alternativa en memoria (Actividad 6)
│       │   │   │   └── DAOFactory.java     # Fábrica abstracta de DAOs (JSON / Memoria)
│       │   │   └── ui/                     # Capa de presentación (Vistas y Controladores)
│       │   │       ├── MateriasActivity.java # CRUD de materias, buscador y resumen de pendientes
│       │   │       ├── MateriaAdapter.java   # RecyclerView Adapter para materias
│       │   │       ├── TareasActivity.java   # CRUD de tareas, validación de fechas, prioridad y buscador
│       │   │       └── TareaAdapter.java     # RecyclerView Adapter con CheckBox y tachado dinámico
│       │   └── res/
│       │       └── layout/
│       │           ├── activity_materias.xml # Formulario superior + buscador + RecyclerView
│       │           ├── item_materia.xml      # Fila con información, botón Tareas y botón Eliminar
│       │           ├── activity_tareas.xml   # Encabezado "X de Y", selector prioridad y formulario
│       │           └── item_tarea.xml        # Fila con CheckBox, títulos, detalle y acciones
│       └── test/java/sv/edu/ufg/agenda/dao/
│           ├── MateriaDAOJsonTest.java       # Pruebas unitarias JUnit con TemporaryFolder
│           └── TareaDAOJsonTest.java         # Pruebas unitarias JUnit con TemporaryFolder
```

---

## 🛡️ 4. Reglas de Negocio Implementadas

1. **Regla 1:** El nombre de la materia y el título de la tarea son campos estrictamente obligatorios (validación con `setError()` en tiempo de ingreso).
2. **Regla 2:** Una tarea solo se puede registrar para una materia existente (validación estricta de `materiaId` en `TareasActivity.onCreate()`; si no existe o es inválida, se finaliza con `finish()`).
3. **Regla 3:** No se puede eliminar una materia que aún posea tareas asociadas (`MateriasActivity.eliminar()` consulta `tareaDAO.listarPorMateria(id)` y bloquea la eliminación mostrando un `Toast` de advertencia).
4. **Regla 4:** La fecha de entrega debe ser una fecha válida en formato calendario estricto `aaaa-mm-dd` (validada con `SimpleDateFormat.setLenient(false)` impidiendo fechas inexistentes como `2026-02-30`).

---

## 🎯 5. Cumplimiento de las 7 Actividades de la Guía

| Actividad | Responsable | Descripción y Solución Implementada |
| :--- | :--- | :--- |
| **1. Reproducir** | Equipo | Proyecto Android configurado desde los Pasos 1 al 6 con dependencias `gson:2.11.0` y `recyclerview:1.3.2`. |
| **2. Completar** | Equipo | Desarrollo íntegro de `TareasActivity`, `TareaAdapter`, `activity_tareas.xml` e `item_tarea.xml` con flujo CRUD completo. |
| **3. Ampliar Entidad** | Rol A | Inclusión del atributo `prioridad` ("Alta", "Media", "Baja") en `Tarea.java`, visualización en UI con un `Spinner` de selección y persistencia automática en `tareas.json`. |
| **4. Búsqueda** | Rol C y B | Búsqueda en tiempo real mediante `TextWatcher` en `MateriasActivity` (`buscarPorNombre`) y en `TareasActivity` (`buscarPorTitulo`). |
| **5. Resumen por Materia** | Equipo | En la lista de materias se muestra el contador `"X tareas pendientes"`. El cálculo se realiza en la capa controladora (`MateriasActivity`) consultando los DAOs, manteniendo los modelos y vistas desacoplados. |
| **6. Reto DAO en Memoria** | Rol C | Creación de `MateriaDAOMemoria` y `TareaDAOMemoria`. En `DAOFactory` basta cambiar la bandera booleana `boolean usarMemoria = false;` a `true` para conmutar sin tocar una sola línea de la UI. |
| **7. Reto Pruebas Unitarias**| Rol B | Implementación de `TareaDAOJsonTest` y `MateriaDAOJsonTest` utilizando `@Rule public TemporaryFolder`, ejecutables en la JVM local sin requerir emulador Android. |

---

## 🧪 6. Ejecución de Pruebas Unitarias

Para ejecutar las pruebas automáticas en la consola o terminal:

```bash
./gradlew test
```

Salida esperada:
```text
BUILD SUCCESSFUL in 10s
38 actionable tasks: 4 executed, 34 up-to-date
```

---

## 📱 7. Verificación en Device Explorer

Una vez ejecutada la aplicación en un emulador o dispositivo físico con depuración USB:
1. En Android Studio, abra la herramienta lateral **Device Explorer** (`View > Tool Windows > Device Explorer`).
2. Navegue a la ruta interna de la aplicación:
   ```text
   /data/data/sv.edu.ufg.agenda/files/datos/
   ```
3. Encontrará los archivos:
   - `materias.json`
   - `tareas.json`
4. Haga clic derecho sobre la carpeta `datos` y seleccione **Synchronize** para actualizar los cambios en caliente.

### Ejemplo de `materias.json`:
```json
[
  {
    "id": 1,
    "nombre": "Programación II",
    "docente": "Ing. Pérez"
  },
  {
    "id": 2,
    "nombre": "Bases de Datos",
    "docente": "Lic. Gómez"
  }
]
```

### Ejemplo de `tareas.json`:
```json
[
  {
    "id": 1,
    "materiaId": 1,
    "titulo": "Guía práctica 3 - DAO JSON",
    "fechaEntrega": "2026-10-20",
    "completada": false,
    "prioridad": "Alta"
  },
  {
    "id": 2,
    "materiaId": 1,
    "titulo": "Investigación sobre Repositories",
    "fechaEntrega": "2026-10-25",
    "completada": true,
    "prioridad": "Media"
  },
  {
    "id": 3,
    "materiaId": 2,
    "titulo": "Diseño de Modelo Relacional",
    "fechaEntrega": "2026-10-28",
    "completada": false,
    "prioridad": "Alta"
  }
]
```

---

## 💡 8. Pregunta de Reflexión del Cierre

> **Pregunta:** *Si mañana la institución exige guardar los datos en SQLite, ¿cuántos archivos de la carpeta `ui` tendrías que modificar?*
> 
> **Respuesta:** **Exactamente CERO (ninguno)**.
> Las pantallas (`MateriasActivity`, `TareasActivity`, `MateriaAdapter`, `TareaAdapter`) dependen única y exclusivamente de los contratos `MateriaDAO` y `TareaDAO`. Bastaría con crear `MateriaDAOSqlite` y `TareaDAOSqlite` (implementando dichas interfaces) y cambiar una sola línea en `DAOFactory`. La capa de presentación ni siquiera se enteraría del cambio de motor de persistencia.
