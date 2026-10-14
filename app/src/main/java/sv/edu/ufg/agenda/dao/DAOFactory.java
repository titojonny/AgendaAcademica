package sv.edu.ufg.agenda.dao;

import java.io.File;

public class DAOFactory {
    private final MateriaDAO materiaDAO;
    private final TareaDAO tareaDAO;

    public DAOFactory(File carpetaDatos) {
        // Reto Actividad 6: Cambiar este booleano en una sola línea para alternar entre JSON y Memoria
        boolean usarMemoria = false;

        if (usarMemoria) {
            this.materiaDAO = new MateriaDAOMemoria();
            this.tareaDAO = new TareaDAOMemoria();
        } else {
            this.materiaDAO = new MateriaDAOJson(new File(carpetaDatos, "materias.json"));
            this.tareaDAO = new TareaDAOJson(new File(carpetaDatos, "tareas.json"));
        }
    }

    public MateriaDAO materias() {
        return materiaDAO;
    }

    public TareaDAO tareas() {
        return tareaDAO;
    }
}
