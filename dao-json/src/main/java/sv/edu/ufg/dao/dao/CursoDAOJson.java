package sv.edu.ufg.dao.dao;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.dao.modelo.Curso;

/**
 * Implementación JSON para la segunda entidad (Curso).
 * Hereda todas las operaciones CRUD y persistencia de JsonDAOBase.
 */
public class CursoDAOJson extends JsonDAOBase<Curso> implements CursoDAO {

    public CursoDAOJson(String rutaArchivo) {
        super(rutaArchivo, Curso.class);
    }

    public CursoDAOJson(Path archivo) {
        super(archivo, Curso.class);
    }

    @Override
    public List<Curso> buscarPorNombre(String texto) {
        List<Curso> resultado = new ArrayList<>();
        for (Curso c : cargar()) {
            if (c.getNombre() != null && c.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}
