package sv.edu.ufg.dao.dao;

import java.util.List;

/**
 * Contrato genérico DAO que define las 5 operaciones CRUD comunes.
 *
 * @param <T> Tipo de entidad administrada
 */
public interface DAO<T> {
    void insertar(T entidad);
    T buscar(int id);
    List<T> listar();
    boolean actualizar(T entidad);
    boolean eliminar(int id);
}
