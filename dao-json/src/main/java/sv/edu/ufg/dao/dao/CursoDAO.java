package sv.edu.ufg.dao.dao;

import java.util.List;
import sv.edu.ufg.dao.modelo.Curso;

/**
 * Contrato específico para la entidad Curso.
 */
public interface CursoDAO extends DAO<Curso> {
    List<Curso> buscarPorNombre(String texto);
}
