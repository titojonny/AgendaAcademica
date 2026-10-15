package sv.edu.ufg.dao.dao;

import java.util.List;
import sv.edu.ufg.dao.modelo.Estudiante;

/**
 * Contrato específico para la entidad Estudiante.
 * Extiende las operaciones CRUD de DAO<Estudiante> y añade búsquedas especializadas.
 */
public interface EstudianteDAO extends DAO<Estudiante> {
    List<Estudiante> buscarPorCarrera(String carrera);
}
