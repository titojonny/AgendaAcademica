# Guía Práctica Integrada: Patrón DAO con Archivos JSON
## De la Consola a una Aplicación Móvil Android

**Universidad Francisco Gavidia (UFG)**  
**Facultad de Ingeniería y Sistemas**  
**Asignatura:** Desarrollo de Aplicaciones Multiplataforma (DAP)  
**Repositorio Oficial:** [https://github.com/titojonny/AgendaAcademica](https://github.com/titojonny/AgendaAcademica)  
**Equipo de Desarrollo:**
- **Jonathan Adonay Alas Girón** (Rol A - Modelo y Contrato)
- **Mauro José Alfaro Amaya** (Rol B - Persistencia JSON y Pruebas)
- **Lenny Adrián Elías Sánchez** (Rol C - Interfaz Móvil y Vistas)

---

## 📌 1. Visión General de la Práctica Integrada

Este repositorio unifica en una sola solución arquitectónica las **dos partes de la guía práctica**:

1. **Parte I (Consola Java Maven - Carpeta `dao-json/`):**
   - Implementación del patrón **Data Access Object (DAO)** en Java puro con Maven y Google Gson.
   - Entidades `Estudiante` y `Curso`, persistiendo en `datos/estudiantes.json` y `datos/cursos.json`.
   - Generalización mediante la clase base abstracta `JsonDAOBase<T>` y la fábrica `DAOFactory`.
   - Doble modo de uso: demostración por lotes y menú interactivo por consola.

2. **Parte II (Aplicación Móvil Android - Módulo `app/`):**
   - Extensión del patrón DAO a una aplicación táctil con dos entidades relacionadas: `Materia` y `Tarea` (1:N).
   - Persistencia en el almacenamiento interno privado del dispositivo (`/data/data/sv.edu.ufg.agenda/files/datos/`).
   - Pantallas `MateriasActivity` y `TareasActivity` con RecyclerView, filtros en tiempo real, prioridad de tareas y validación estricta de 4 reglas de negocio.
   - Retos resueltos: DAOs alternativos en memoria (`MateriaDAOMemoria`, `TareaDAOMemoria`) y pruebas unitarias automáticas JUnit con `TemporaryFolder`.

---

## 🏛️ 2. Arquitectura del Patrón DAO en Ambos Entornos

```text
==================================================================================================
                                    PARTE I: CONSOLA JAVA (dao-json)
==================================================================================================
Cliente: Main.java (o Menú) ----> DAOFactory.estudiantes() / cursos()
                                           |
                                           v
            +------------------------------+------------------------------+
            | Contratos (Interfaces)                                      |
            |   DAO<T>  <---  EstudianteDAO / CursoDAO                    |
            +------------------------------+------------------------------+
                                           |
                                           v
            +-------------------------------------------------------------+
            | Base Genérica: JsonDAOBase<T extends Entidad>               |
            |   (cargar, guardar, siguienteId, CRUD con Gson y Java NIO)  |
            +------------------------------+------------------------------+
                                           |
                                           v
                                EstudianteDAOJson / CursoDAOJson
                                           |
                                           v
                            datos/estudiantes.json | datos/cursos.json

==================================================================================================
                                    PARTE II: ANDROID MÓVIL (app)
==================================================================================================
Vistas / UI: MateriasActivity / TareasActivity
      |
      +---> AgendaApp.daos() ---> DAOFactory (centralizado)
                                           |
                                           v
            +------------------------------+------------------------------+
            | Contratos (Interfaces)                                      |
            |   DAO<T>  <---  MateriaDAO / TareaDAO                       |
            +------------------------------+------------------------------+
                                           |
                  +------------------------+------------------------+
                  |                                                 |
                  v                                                 v
    JsonDAOBase<T> (con java.io)                        DAOs en Memoria (Actividad 6)
                  |                                                 |
                  v                                                 v
     MateriaDAOJson / TareaDAOJson                   MateriaDAOMemoria / TareaDAOMemoria
                  |
                  v
/data/data/sv.edu.ufg.agenda/files/datos/ (materias.json / tareas.json)
```

---

## 📂 3. Estructura Unificada del Proyecto

```text
AgendaAcademica/
├── dao-json/                               # PARTE I: PROYECTO CONSOLA MAVEN
│   ├── pom.xml                             # Dependencias Gson 2.11.0, Java 17
│   ├── datos/
│   │   ├── estudiantes.json                # Persistencia de estudiantes
│   │   └── cursos.json                     # Persistencia de cursos (Actividad Pág. 24)
│   └── src/main/java/sv/edu/ufg/dao/
│       ├── Main.java                       # Cliente consola con demo y menú interactivo
│       ├── modelo/
│       │   ├── Entidad.java                # Interfaz base con getId() y setId()
│       │   ├── Estudiante.java             # Entidad Estudiante con correo
│       │   └── Curso.java                  # Segunda entidad Curso con UV
│       └── dao/
│           ├── DAO.java                    # Contrato genérico CRUD
│           ├── EstudianteDAO.java          # Contrato Estudiante (buscarPorCarrera)
│           ├── CursoDAO.java               # Contrato Curso (buscarPorNombre)
│           ├── JsonDAOBase.java            # Reto: Base genérica abstracta
│           ├── EstudianteDAOJson.java      # Implementación JSON para estudiantes
│           ├── CursoDAOJson.java           # Implementación JSON para cursos
│           └── DAOFactory.java             # Fábrica de DAOs en consola
│
├── app/                                    # PARTE II: APLICACIÓN MÓVIL ANDROID
│   ├── build.gradle                        # Gson 2.11.0, RecyclerView 1.3.2, minSdk 24
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml         # Registro de AgendaApp y Activities
│       │   ├── java/sv/edu/ufg/agenda/
│       │   │   ├── AgendaApp.java          # Application con ciclo de vida de DAOFactory
│       │   │   ├── modelo/                 # Entidad, Materia, Tarea (con prioridad)
│       │   │   ├── dao/                    # DAO<T>, MateriaDAO, TareaDAO, JsonDAOBase<T>,
│       │   │   │                           # MateriaDAOJson, TareaDAOJson, DAOFactory,
│       │   │   │                           # MateriaDAOMemoria, TareaDAOMemoria
│       │   │   └── ui/                     # MateriasActivity, MateriaAdapter,
│       │   │                               # TareasActivity, TareaAdapter
│       │   └── res/layout/                 # activity_materias, item_materia,
│       │                                   # activity_tareas, item_tarea
│       └── test/java/sv/edu/ufg/agenda/dao/# Pruebas unitarias JUnit con TemporaryFolder
│           ├── MateriaDAOJsonTest.java
│           └── TareaDAOJsonTest.java
│
├── docs/                                   # Evidencias y documentación
│   └── img/                                # Capturas de emulador y Device Explorer
├── DOCUMENTACION_ENTREGA_GUIA_DAO.md       # Memoria técnica completa de la práctica
└── INFORME_ENTREGA_UFG.html                # Informe oficial formateado para impresión PDF
```

---

## 🚀 4. Guía de Ejecución

### A. Para ejecutar la Parte I (Consola Java):
Navega a la carpeta `dao-json/`:
```bash
cd dao-json
```

**Modo Demostración Automática:**
```bash
mvn compile exec:java -Dexec.mainClass="sv.edu.ufg.dao.Main"
```

**Modo Menú Interactivo por Consola:**
```bash
mvn compile exec:java -Dexec.mainClass="sv.edu.ufg.dao.Main" -Dexec.args="--menu"
```

### B. Para ejecutar la Parte II (Móvil Android):
1. Abre la carpeta raíz `AgendaAcademica` en **Android Studio**.
2. Presiona **Sync Project with Gradle Files**.
3. Selecciona un emulador (minSdk 24+) o un teléfono con depuración USB y presiona **Run 'app'** (`Shift + F10`).

**Para ejecutar las pruebas unitarias JUnit (sin emulador):**
```bash
./gradlew test
```

---

## 🛡️ 5. Reglas de Negocio Validadas en la App Móvil

1. **Regla 1:** El nombre de la materia y el título de la tarea son obligatorios (`setError`).
2. **Regla 2:** Una tarea solo se puede registrar para una materia existente (si `materiaId` es inválido, cierra con `finish()`).
3. **Regla 3:** No se puede eliminar una materia que posea tareas asociadas (se verifica con `tareaDAO.listarPorMateria()` y bloquea con `Toast`).
4. **Regla 4:** Fecha de entrega obligatoria y calendario estricto `aaaa-mm-dd` (`SimpleDateFormat.setLenient(false)` rechaza días como `2026-02-30`).

---

## 💡 6. Pregunta de Reflexión del Cierre (Pág. 27)

> **Pregunta:** *Si mañana la institución exige guardar los datos en una base de datos (SQLite en Android o MySQL con JDBC en consola), ¿cuántas clases tendrías que modificar en consola y cuántos archivos de la carpeta `ui` en la app móvil?*
> 
> **Respuesta:**
> - En consola: **Una sola clase** (`DAOFactory.java`, cambiando la instanciación de los DAOs). El cliente `Main` permanece intacto.
> - En la app móvil: **Exactamente CERO (0) archivos de la carpeta `ui`**.
> 
> **Justificación Técnica:**  
> Las capas consumidoras (`Main` en consola y `MateriasActivity`, `TareasActivity`, adaptadores en Android) se programaron de forma 100% agnóstica contra las interfaces (`EstudianteDAO`, `CursoDAO`, `MateriaDAO`, `TareaDAO`). Cambiar el motor de persistencia a SQLite, Room o JDBC solo requiere nuevas clases implementando dichos contratos y actualizar la fábrica centralizada.
