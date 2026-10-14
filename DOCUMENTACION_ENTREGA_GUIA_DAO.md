# INFORME TÉCNICO Y MEMORIA DE ENTREGA
## Guía Práctica: Patrón DAO con Archivo JSON en Aplicación Móvil

**Institución:** Universidad Francisco Gavidia (UFG)  
**Facultad:** Facultad de Ingeniería y Sistemas  
**Asignatura:** Desarrollo de Aplicaciones Multiplataforma (DAP)  
**Proyecto:** Agenda Académica Móvil  
**Equipo de Desarrollo:**
- **Jonathan Adonay Alas Girón** (Rol A - Modelo y Contrato)
- **Mauro José Alfaro Amaya** (Rol B - Persistencia JSON y Pruebas)
- **Lenny Adrián Elías Sánchez** (Rol C - Interfaz Móvil y Vistas)
**Lenguaje y Plataforma:** Java / Android Studio (minSdk 24, compileSdk 34)

---

## 1. Resumen Ejecutivo y Alcance

El presente documento recopila la verificación técnica, diseño arquitectónico, cumplimiento de requisitos y resolución de actividades de la **Guía Práctica: Patrón DAO con archivo JSON en una aplicación móvil**.

El objetivo principal fue desarrollar una aplicación móvil con persistencia desacoplada utilizando el patrón de diseño **Data Access Object (DAO)**. Los datos se almacenan en formato JSON estructurado en el almacenamiento interno privado de la aplicación (`/data/data/sv.edu.ufg.agenda/files/datos/`). A través de una clase base genérica (`JsonDAOBase<T>`), una fábrica centralizada (`DAOFactory`) y contratos basados en interfaces Java, se logró una independencia total entre la capa de interfaz de usuario y la persistencia de datos.

---

## 2. Matriz de Cumplimiento de Requisitos de la Guía

A continuación se detalla la verificación punto por punto contra las especificaciones de la guía:

| Sección / Paso | Requisito de la Guía | Estado | Evidencia en el Código |
| :--- | :--- | :---: | :--- |
| **Paso 1** | Dependencias Gson y RecyclerView, paquete `sv.edu.ufg.agenda`, minSdk 24 | **Cumplido** | [app/build.gradle](file:///c:/Users/adona/Downloads/AgendaAcademica/app/build.gradle) |
| **Paso 2** | Interfaz `Entidad` y modelos `Materia`, `Tarea` con constructores vacíos y getters/setters | **Cumplido** | [Entidad.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/modelo/Entidad.java), [Materia.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/modelo/Materia.java), [Tarea.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/modelo/Tarea.java) |
| **Paso 2** | Interfaces del contrato `DAO<T>`, `MateriaDAO`, `TareaDAO` | **Cumplido** | [DAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/DAO.java), [MateriaDAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/MateriaDAO.java), [TareaDAO.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/TareaDAO.java) |
| **Paso 3** | Clase genérica `JsonDAOBase<T>` que centraliza CRUD, lectura/escritura JSON y autoincremento | **Cumplido** | [JsonDAOBase.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/JsonDAOBase.java) |
| **Paso 4** | Implementaciones `MateriaDAOJson` y `TareaDAOJson` | **Cumplido** | [MateriaDAOJson.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/MateriaDAOJson.java), [TareaDAOJson.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/TareaDAOJson.java) |
| **Paso 5** | `DAOFactory` y registro de `AgendaApp` en `AndroidManifest.xml` | **Cumplido** | [DAOFactory.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/DAOFactory.java), [AgendaApp.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/AgendaApp.java), [AndroidManifest.xml](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/AndroidManifest.xml) |
| **Paso 6** | `MateriasActivity`, `MateriaAdapter`, layouts `activity_materias.xml` y `item_materia.xml` | **Cumplido** | [MateriasActivity.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/MateriasActivity.java), [MateriaAdapter.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/MateriaAdapter.java) |
| **Paso 7** | `TareasActivity`, `TareaAdapter`, encabezado "X de Y", ordenación por fecha y validaciones | **Cumplido** | [TareasActivity.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/TareasActivity.java), [TareaAdapter.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/ui/TareaAdapter.java) |
| **Paso 8** | Persistencia interna en `/data/data/sv.edu.ufg.agenda/files/datos/` | **Cumplido** | [materias.json](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_device_explorer_materias.png) y [tareas.json](file:///c:/Users/adona/Downloads/AgendaAcademica/docs/img/captura_device_explorer_tareas.png) |
| **Paso 9** | Integración del equipo y desacoplamiento verificado (UI no importa clases DAOJson) | **Cumplido** | Cero importaciones de clases concretas en el paquete `ui` |

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

## 4. Resolución Detallada de las Actividades para el Estudiante (Paso 10)

### Actividad 1: Reproducir (Equipo)
- Se configuró la estructura base según los Pasos 1 al 6. Los DAOs leen y escriben en la carpeta `datos` del almacenamiento interno (`getFilesDir()`).
- Al agregar una materia, el archivo `materias.json` se crea con formato legible y sangrado (*pretty-printing*).

### Actividad 2: Completar Pantalla de Tareas (Equipo)
- Se desarrolló `TareasActivity` con:
  - Formulario completo para agregar y editar tareas.
  - Lista de tareas con RecyclerView filtradas por `materiaId` y ordenadas cronológicamente por `fechaEntrega`.
  - Encabezado con contador dinámico `X de Y completadas`.
  - CheckBox en cada fila que permite alternar el estado completada/pendiente con actualización inmediata en `tareas.json` y efecto tachado en el título.

### Actividad 3: Ampliar la Entidad Tarea con Prioridad (Rol A)
- **Modificación en el Modelo:** Se incorporó el atributo `private String prioridad;` ("Alta", "Media", "Baja") en `Tarea.java`. Se incluyó constructor con valor predeterminado `"Media"` y sus respectivos getters y setters.
- **Interfaz Gráfica:** Se añadió un componente `Spinner` en `activity_tareas.xml` cargado con los valores `Alta`, `Media` y `Baja`.
- **Persistencia:** Al guardarse o serializarse con Gson, el campo `prioridad` queda automáticamente registrado en `tareas.json`.

### Actividad 4: Búsqueda en Tiempo Real (Rol C con apoyo de Rol B)
- **Contratos:**
  - `MateriaDAO.java` declara `List<Materia> buscarPorNombre(String texto);`.
  - `TareaDAO.java` declara `List<Tarea> buscarPorTitulo(String texto);` y `List<Tarea> buscarPorTitulo(int materiaId, String texto);`.
- **Implementación DAO:** Ambas clases (`MateriaDAOJson` y `TareaDAOJson`) recorren los elementos filtrando con `toLowerCase().contains(texto.toLowerCase())`.
- **Experiencia de Usuario (UI):** En lugar de requerir pulsar un botón, se vincularon componentes `EditText` con listeners `TextWatcher`. La lista se filtra en tiempo real conforme el usuario teclea.

### Actividad 5: Resumen por Materia (Tareas Pendientes)
- **Requisito:** Mostrar bajo el nombre de cada materia cuántas tareas pendientes posee.
- **Implementación:**
  - En `MateriasActivity.java`, durante el método `refrescar()`, se construye un mapa `Map<Integer, Integer> mapaPendientes` calculando cuántas tareas tienen `!t.isCompletada()` para cada materia.
  - En `item_materia.xml` y `MateriaAdapter.java`, se renderiza:
    ```text
    Programación II
    Ing. Pérez
    (2 tareas pendientes)
    ```
- **Justificación de Arquitectura (¿En qué capa va el cálculo?):**
  - **Decisión:** El cálculo reside en la capa controladora de la vista (`MateriasActivity`), coordinando las consultas a `MateriaDAO` y `TareaDAO`.
  - **Justificación:** Los modelos de dominio (`Materia`, `Tarea`) deben permanecer como objetos planos puros (POJO / DTO) sin acoplamiento a DAOs. Por su parte, `MateriaDAO` no debe tener dependencia directa de `TareaDAO` para no romper el principio de responsabilidad única (SRP). El controlador o ViewModel es el lugar idóneo para orquestar la agregación de datos para la vista.

### Actividad 6: Reto - DAO en Memoria (Rol C)
- Se crearon las clases [MateriaDAOMemoria.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/MateriaDAOMemoria.java) y [TareaDAOMemoria.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/TareaDAOMemoria.java) implementando los contratos `MateriaDAO` y `TareaDAO` utilizando un `ArrayList<T>` privado y un autoincremental de IDs.
- En [DAOFactory.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/main/java/sv/edu/ufg/agenda/dao/DAOFactory.java) se definió:
  ```java
  boolean usarMemoria = false; // Alternar a true para modo memoria
  if (usarMemoria) {
      this.materiaDAO = new MateriaDAOMemoria();
      this.tareaDAO = new TareaDAOMemoria();
  } else {
      this.materiaDAO = new MateriaDAOJson(new File(carpetaDatos, "materias.json"));
      this.tareaDAO = new TareaDAOJson(new File(carpetaDatos, "tareas.json"));
  }
  ```
- **¿En qué situaciones del trabajo en equipo sirve?**
  1. Permite al integrante de interfaz móvil (Rol C) avanzar en el diseño y prueba de layouts sin esperar que el integrante de persistencia (Rol B) termine la lectura/escritura de archivos.
  2. Facilita la ejecución de pruebas rápidas de interfaz (instrumentadas o manuales) en entornos sin permisos de almacenamiento o donde se desee un estado limpio en cada ejecución sin borrar archivos físicos.
  3. Sirve como doble de prueba (*mock/stub*) en entornos de desarrollo ágil.

### Actividad 7: Reto - Pruebas Unitarias JUnit (Rol B)
- Se diseñaron dos suites de pruebas unitarias completas:
  - [TareaDAOJsonTest.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/test/java/sv/edu/ufg/agenda/dao/TareaDAOJsonTest.java)
  - [MateriaDAOJsonTest.java](file:///c:/Users/adona/Downloads/AgendaAcademica/app/src/test/java/sv/edu/ufg/agenda/dao/MateriaDAOJsonTest.java)
- **Características de la prueba:**
  - Hacen uso de la regla `@Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();`.
  - Crean y limpian archivos temporales en el sistema de archivos del sistema operativo anfitrión.
  - Ejecutan las operaciones `insertar`, `buscar`, `buscarPorNombre`, `listarPorMateria`, `actualizar` y `eliminar`.
  - Se ejecutan en la máquina virtual Java (JVM) estándar mediante `./gradlew test` en apenas 10 segundos, sin necesidad de emulador Android ni dispositivo físico.

---

## 5. Respuesta a la Pregunta de Reflexión del Cierre (Sección 13)

> **Pregunta de la Guía:** *Si mañana la institución exige guardar los datos en SQLite, ¿cuántos archivos de la carpeta `ui` tendrías que modificar?*
>
> **Respuesta:** **CERO (0) archivos.**
> 
> **Explicación técnica:**
> Debido a que las clases `MateriasActivity`, `TareasActivity`, `MateriaAdapter` y `TareaAdapter` fueron programadas estrictamente contra las interfaces `MateriaDAO` y `TareaDAO`, desconocen por completo el mecanismo de persistencia subyacente. Para migrar a SQLite o Room, los únicos cambios necesarios serían:
> 1. Crear las clases `MateriaDAOSqlite` y `TareaDAOSqlite` implementando `MateriaDAO` y `TareaDAO`.
> 2. Cambiar la instanciación en `DAOFactory` para que entregue los DAOs de SQLite en lugar de los DAOs de JSON.
> Ninguna línea de layout, adapter ni actividad sufriría modificaciones.

---

## 6. Conclusiones

La implementación del patrón DAO junto a una clase base genérica demostró ser una solución arquitectónica sólida para el desarrollo en equipo. Evitó la duplicación de código de lectura/escritura JSON en un 100% y permitió la independencia entre la persistencia y la interfaz gráfica, cumpliendo a cabalidad con todos los criterios de evaluación, competencias y actividades propuestas en la guía académica.
