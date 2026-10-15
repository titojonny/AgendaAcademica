package sv.edu.ufg.dao.modelo;

/**
 * Contrato base para todas las entidades que poseen un identificador numérico.
 * Permite que la clase genérica JsonDAOBase maneje el autoincremento y búsqueda de IDs.
 */
public interface Entidad {
    int getId();
    void setId(int id);
}
