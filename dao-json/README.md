# Práctica DAO JSON - Parte I (Consola Java)

Proyecto desarrollado para la **Facultad de Ingeniería y Sistemas** (Materia: Desarrollo de Aplicaciones Multiplataforma - DAP).

## 📌 Arquitectura Implementada

Se aplica el patrón **Data Access Object (DAO)** junto con una **clase base genérica** y una **fábrica (DAOFactory)** para desacoplar totalmente la lógica de persistencia (archivos JSON) del programa cliente:

- **Modelo**:
  - `sv.edu.ufg.dao.modelo.Entidad` (contrato base para entidades con ID numérico).
  - `sv.edu.ufg.dao.modelo.Estudiante` (con atributos `id`, `nombre`, `carrera` y el adicional `correo`).
  - `sv.edu.ufg.dao.modelo.Curso` (segunda entidad: `id`, `codigo`, `nombre`, `uv`).
- **Contratos (Interfaces)**:
  - `sv.edu.ufg.dao.dao.DAO<T>` (interfaz genérica con las 5 operaciones CRUD fundamentales).
  - `sv.edu.ufg.dao.dao.EstudianteDAO` (extiende `DAO<Estudiante>` y añade `buscarPorCarrera`).
  - `sv.edu.ufg.dao.dao.CursoDAO` (extiende `DAO<Curso>` y añade `buscarPorNombre`).
- **Persistencia y Reutilización (Reto de la Guía)**:
  - `sv.edu.ufg.dao.dao.JsonDAOBase<T>` (clase genérica abstracta que implementa `cargar()`, `guardar()`, autoincremento de IDs y el CRUD completo).
  - `sv.edu.ufg.dao.dao.EstudianteDAOJson` y `sv.edu.ufg.dao.dao.CursoDAOJson` (heredan de `JsonDAOBase<T>` sin duplicar código de archivos).
- **Fábrica**:
  - `sv.edu.ufg.dao.dao.DAOFactory` (centraliza la creación de los DAOs).
- **Cliente**:
  - `sv.edu.ufg.dao.Main` (consume exclusivamente las interfaces a través de la fábrica).

## 🚀 Cómo ejecutar

### Modo Demostración Automática (Estudiantes + Cursos)
```bash
mvn compile exec:java -Dexec.mainClass="sv.edu.ufg.dao.Main"
```
o ejecutando directamente la clase `Main` desde tu IDE (IntelliJ, Eclipse, NetBeans, VS Code).

### Modo Menú Interactivo por Consola
```bash
mvn compile exec:java -Dexec.mainClass="sv.edu.ufg.dao.Main" -Dexec.args="--menu"
```

## 📁 Archivos generados
- `datos/estudiantes.json`: persistencia de estudiantes.
- `datos/cursos.json`: persistencia de cursos.
