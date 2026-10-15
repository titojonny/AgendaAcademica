package sv.edu.ufg.dao.dao;

import java.nio.file.Path;

/**
 * Fábrica centralizada de DAOs para la aplicación de consola.
 * Centraliza la instanciación para que si el almacén cambia (por ejemplo a JDBC o SQLite),
 * solo se modifique esta fábrica sin tocar el cliente (Main).
 */
public class DAOFactory {
    private final EstudianteDAO estudianteDAO;
    private final CursoDAO cursoDAO;

    public DAOFactory() {
        this("datos");
    }

    public DAOFactory(String rutaCarpeta) {
        Path carpeta = Path.of(rutaCarpeta);
        this.estudianteDAO = new EstudianteDAOJson(carpeta.resolve("estudiantes.json"));
        this.cursoDAO = new CursoDAOJson(carpeta.resolve("cursos.json"));
    }

    public EstudianteDAO estudiantes() {
        return estudianteDAO;
    }

    public CursoDAO cursos() {
        return cursoDAO;
    }
}
