# INFORME TÉCNICO Y MEMORIA DE ENTREGA
## Guía Práctica Integrada: Patrón DAO con Archivo JSON
### De la Consola a una Aplicación Móvil Android

**Institución:** Universidad Francisco Gavidia (UFG)  
**Facultad:** Facultad de Ingeniería y Sistemas  
**Asignatura:** Desarrollo de Aplicaciones Multiplataforma (DAP)  
**Repositorio GitHub:** [https://github.com/titojonny/AgendaAcademica](https://github.com/titojonny/AgendaAcademica)  
**Equipo de Desarrollo:**
- **Jonathan Adonay Alas Girón** (Rol A - Modelo y Contrato)
- **Mauro José Alfaro Amaya** (Rol B - Persistencia JSON y Pruebas)
- **Lenny Adrián Elías Sánchez** (Rol C - Interfaz Móvil y Vistas)
**Plataformas:** Java 17+ con Maven (Parte I) y Android Studio Java (Parte II, minSdk 24, compileSdk 34)

---

## 1. Resumen Ejecutivo y Alcance

El presente documento recopila la verificación técnica, diseño arquitectónico, cumplimiento de requisitos y resolución de actividades de la **Guía Práctica Integrada: Patrón DAO con archivos JSON, de la consola a una aplicación móvil**.

La práctica unifica dos fases complementarias:
1. **Parte I (Consola Java - Subcarpeta `dao-json/`):** Construcción del patrón DAO con Maven y Gson para persistir datos en archivos `.json` en disco local (`datos/estudiantes.json` y `datos/cursos.json`), generalización mediante `JsonDAOBase<T>` y centralización en `DAOFactory`.
2. **Parte II (Móvil Android - Módulo `app/`):** Traslado del mismo patrón y contratos a una app táctil Android con entidades relacionadas (`Materia` y `Tarea`), persistiendo en el almacenamiento interno privado (`/data/data/sv.edu.ufg.agenda/files/datos/`) con ciclo de vida en `AgendaApp`.

---

## 2. Matriz de Cumplimiento de Requisitos de la Guía

### A. Parte I: Consola Java (Pasos 1 al 6 y Actividades Pág. 24-25)

| Paso / Actividad | Requisito de la Guía | Estado | Evidencia en el Código |
| :--- | :--- | :---: | :--- |
| **Paso 1** | Proyecto Maven con dependencia Gson 2.11.0 y Java 17 | **Cumplido** | [dao-json/pom.xml](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/pom.xml) |
| **Paso 2** | Entidad `Estudiante` con constructores, getters, setters y `toString()` | **Cumplido** | [Estudiante.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/modelo/Estudiante.java) |
| **Paso 3** | Interfaz del contrato `EstudianteDAO` (CRUD completo y desacoplado) | **Cumplido** | [EstudianteDAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/dao/EstudianteDAO.java) |
| **Paso 4** | Implementación `EstudianteDAOJson` con carga, guardado y autoincremento | **Cumplido** | [EstudianteDAOJson.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/dao/EstudianteDAOJson.java) |
| **Paso 5** | Cliente `Main` que consume exclusivamente la interfaz `EstudianteDAO` | **Cumplido** | [Main.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/Main.java) |
| **Paso 6** | Verificación de persistencia en `datos/estudiantes.json` | **Cumplido** | [estudiantes.json](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/datos/estudiantes.json) |
| **Actividad 1** | Ampliar entidad con atributo `correo` en `Estudiante` | **Cumplido** | Campo `correo` registrado y serializado en `Estudiante.java` |
| **Actividad 2** | Nuevo método `buscarPorCarrera(String carrera)` en contrato e implementación | **Cumplido** | Declarado en `EstudianteDAO.java` e implementado con filtros case-insensitive |
| **Actividad 3** | Menú interactivo por consola (opciones 1-5 y 0 salir) que usa solo la interfaz | **Cumplido** | Modo `--menu` en `Main.java` con menús completos para todas las operaciones |
| **Actividad 4** | Segunda entidad `Curso` con su `CursoDAO` y `CursoDAOJson` | **Cumplido** | [Curso.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/modelo/Curso.java), [CursoDAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/dao/CursoDAO.java), [CursoDAOJson.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/dao/CursoDAOJson.java) |
| **Reto Pág. 25** | Eliminar código repetido entre DAOs creando `JsonDAOBase<T>` genérico | **Cumplido** | [JsonDAOBase.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/dao/JsonDAOBase.java) en consola |
| **Sugerencia P.24** | Centralizar creación en `DAOFactory` para consola | **Cumplido** | [DAOFactory.java](file:///c:/Users/adona/Downloads/AgendaAcademica/dao-json/src/main/java/sv/edu/ufg/dao/dao/DAOFactory.java) |

---

### B. Parte II: Aplicación Móvil Android (Pasos 7 al 15 y Actividades Pág. 25)

| Paso / Actividad | Requisito de la Guía | Estado | Evidencia en el Código |
| :--- | :--- | :---: | :--- |
| **Paso 7** | Dependencias Gson y RecyclerView, paquete `sv.edu.ufg.agenda`, minSdk 24 | **Cumplido** | [app/build.gradle](file:///c:/Users/adona/Downloads/AgendaAcademica/app/build.gradle) |
| **Paso 8** | Interfaz `Entidad` y modelos `Materia`, `Tarea` con constructores vacíos | **Cumplido** | [Entidad.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/modelo/Entidad.java), [Materia.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/modelo/Materia.java), [Tarea.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/modelo/Tarea.java) |
| **Paso 8** | Interfaces del contrato `DAO<T>`, `MateriaDAO`, `TareaDAO` | **Cumplido** | [DAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/DAO.java), [MateriaDAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/MateriaDAO.java), [TareaDAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/TareaDAO.java) |
| **Paso 9** | Clase genérica `JsonDAOBase<T>` que centraliza CRUD y streams en Android | **Cumplido** | [JsonDAOBase.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/JsonDAOBase.java) |
| **Paso 10** | Implementaciones `MateriaDAOJson` y `TareaDAOJson` | **Cumplido** | [MateriaDAOJson.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/MateriaDAOJson.java), [TareaDAOJson.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/TareaDAOJson.java) |
| **Paso 11** | `DAOFactory` y registro de `AgendaApp` en `AndroidManifest.xml` | **Cumplido** | [DAOFactory.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/DAOFactory.java), [AgendaApp.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/AgendaApp.java), [AndroidManifest.xml](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/AndroidManifest.xml) |
| **Paso 12** | `MateriasActivity`, `MateriaAdapter`, layouts `activity_materias.xml` y `item_materia.xml` | **Cumplido** | [MateriasActivity.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/MateriasActivity.java), [MateriaAdapter.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/MateriaAdapter.java) |
| **Paso 13** | `TareasActivity`, `TareaAdapter`, encabezado "X de Y", ordenación por fecha y validaciones | **Cumplido** | [TareasActivity.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/TareasActivity.java), [TareaAdapter.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/TareaAdapter.java) |
| **Paso 14** | Persistencia interna en `/data/data/sv.edu.ufg.agenda/files/datos/` | **Cumplido** | [materias.json](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_device_explorer_materias.png) y [tareas.json](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_device_explorer_tareas.png) |
| **Paso 15** | Integración del equipo y desacoplamiento verificado (UI no importa clases DAOJson) | **Cumplido** | Cero importaciones de clases concretas en el paquete `ui` |

### Evidencias Gráficas Incorporadas
- [Captura Device Explorer - Materias](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_device_explorer_materias.png) (Actividad 1)
- [Captura Device Explorer - Tareas](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_device_explorer_tareas.png) (Actividad 2 con 3 tareas de 2 materias)
- [Captura Emulador - MateriasActivity](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_app_materias.png) (Actividades 4 y 5)
- [Captura Emulador - TareasActivity Materia 1](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_app_tareas_materia1.png) (Actividades 2, 3 y 4)
- [Captura Emulador - TareasActivity Materia 2](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_app_tareas_materia2.png) (Actividades 2 y 3)

---

## 3. Validación de las 4 Reglas de Negocio del Caso de Estudio

1. **Regla 1: Nombre de materia y título de tarea obligatorios**
   - En `MateriasActivity.java`: Si el campo de texto está vacío, se aplica `etNombre.setError("El nombre es obligatorio")` y se aborta el guardado.
   - En `TareasActivity.java`: Si el campo de título está vacío, se aplica `etTitulo.setError("El título es obligatorio")` y se aborta el guardado.

2. **Regla 2: Una tarea solo se puede crear para una materia que existe**
   - En `TareasActivity.java`: Al abrir la actividad en `onCreate()`, se extrae el parámetro `materiaId = getIntent().getIntExtra("materiaId", -1)`. Se realiza la comprobación `materia = materiaDAO.buscar(materiaId)`. Si retorna `null`, se notifica al usuario y se ejecuta `finish()` de inmediato, impidiendo crear tareas huérfanas.

3. **Regla 3: No se puede eliminar una materia que todavía tiene tareas asociadas**
   - En `MateriasActivity.java`: El método `eliminar(Materia materia)` consulta previamente `if (!tareaDAO.listarPorMateria(materia.getId()).isEmpty())`. Si existen tareas vinculadas, muestra un mensaje informativo mediante `Toast.makeText("No se puede eliminar: la materia tiene tareas", Toast.LENGTH_LONG).show()` y cancela la operación de borrado.

4. **Regla 4: La fecha de entrega debe ser una fecha válida con formato aaaa-mm-dd**
   - En `TareasActivity.java`: Se implementó el método `fechaValida(String texto)` utilizando `SimpleDateFormat("yyyy-MM-dd", Locale.US)` configurando explícitamente `formato.setLenient(false)`. Esto asegura que fechas inexistentes en el calendario (como `2026-02-30` o meses superiores a 12) lancen un `ParseException` y sean rechazadas con un mensaje claro al usuario.

---

## 4. Resolución Detallada de las Actividades para el Estudiante

### Actividades Parte I (Consola)
- **Ampliar entidad Estudiante con correo:** Atributo agregado, constructores sobrecargados y persistencia en `datos/estudiantes.json`.
- **Nuevo método buscarPorCarrera:** Filtrado dinámico en memoria de la lista deserializada.
- **Menú por consola:** Doble ejecución en `Main.java` (modo automático y modo interactivo con `--menu`).
- **Segunda entidad Curso:** Atributos `id`, `codigo`, `nombre`, `uv`, con su `CursoDAO` y `CursoDAOJson`.
- **Reto `JsonDAOBase<T>`:** Centraliza `cargar()`, `guardar()`, `siguienteId()` y el CRUD completo para `Estudiante` y `Curso`, eliminando la duplicación de código en un 100%.

### Actividades Parte II (Móvil Android)
- **Actividad 1 (Reproducir):** Estructura base funcionando en emulador con almacenamiento en `getFilesDir()/datos/materias.json`.
- **Actividad 2 (Completar Tareas):** Pantalla `TareasActivity` con lista ordenada cronológicamente, contador dinámico "X de Y completadas" y CheckBox con tachado dinámico.
- **Actividad 3 (Prioridad en Tarea):** Atributo `prioridad` ("Alta", "Media", "Baja") con `Spinner` en la interfaz y persistencia en `tareas.json`.
- **Actividad 4 (Búsqueda en Tiempo Real):** `TextWatcher` en ambas pantallas para filtrado inmediato al escribir.
- **Actividad 5 (Resumen por Materia):** Conteo de tareas pendientes renderizado en cada fila de materia, calculado en la capa controladora (`MateriasActivity`) sin violar la responsabilidad única de los modelos ni DAOs.
- **Actividad 6 (Reto DAOs en Memoria):** Clases `MateriaDAOMemoria` y `TareaDAOMemoria` conmutables con una variable booleana en `DAOFactory`.
- **Actividad 7 (Reto Pruebas Unitarias JUnit):** Suites de test `TareaDAOJsonTest` y `MateriaDAOJsonTest` con `@Rule public TemporaryFolder`, ejecutables en JVM local en segundos mediante `./gradlew test`.

---

## 5. Respuesta a la Pregunta de Reflexión del Cierre (Pág. 27)

> **Pregunta:** *Si mañana la institución exige guardar los datos en una base de datos (SQLite en Android o MySQL en consola), ¿cuántas clases tendrías que modificar en tu proyecto de consola y cuántos archivos de la carpeta ui en tu app móvil?*
> 
> **Respuesta:**
> - En consola: **Una sola clase** (`DAOFactory.java`, cambiando la instanciación de los DAOs a la nueva implementación JDBC). El cliente `Main` permanece inalterado.
> - En la app móvil: **Exactamente CERO (0) archivos de la carpeta `ui`**.
> 
> **Explicación técnica:**
> Las pantallas (`MateriasActivity`, `TareasActivity`, `MateriaAdapter`, `TareaAdapter`) y el cliente de consola fueron programados de forma 100% agnóstica contra las interfaces (`EstudianteDAO`, `CursoDAO`, `MateriaDAO`, `TareaDAO`). Para migrar a SQLite o Room solo se requiere programar `MateriaDAOSqlite` y `TareaDAOSqlite` y cambiar una línea en `DAOFactory`. La interfaz gráfica se mantiene completamente intacta.

---

## 6. Conclusiones

La implementación del patrón DAO junto a una clase base genérica demostró ser una solución arquitectónica sólida para el desarrollo en equipo. Evitó la duplicación de código de lectura/escritura JSON en un 100% y permitió la independencia entre la persistencia y la interfaz gráfica, cumpliendo a cabalidad con todos los criterios de evaluación, competencias y actividades propuestas en la guía académica.
