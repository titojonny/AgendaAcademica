package sv.edu.ufg.dao.dao;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.dao.modelo.Estudiante;

/**
 * Implementación JSON para Estudiante.
 * Hereda todas las operaciones CRUD y persistencia de JsonDAOBase.
 */
public class EstudianteDAOJson extends JsonDAOBase<Estudiante> implements EstudianteDAO {

    public EstudianteDAOJson(String rutaArchivo) {
        super(rutaArchivo, Estudiante.class);
    }

    public EstudianteDAOJson(Path archivo) {
        super(archivo, Estudiante.class);
    }

    @Override
    public List<Estudiante> buscarPorCarrera(String carrera) {
        List<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : cargar()) {
            if (e.getCarrera() != null && e.getCarrera().equalsIgnoreCase(carrera)) {
                resultado.add(e);
            }
        }
        return resultado;
    }
}
