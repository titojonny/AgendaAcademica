package sv.edu.ufg.agenda.dao;

import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.modelo.Tarea;

/**
 * Implementación en memoria de TareaDAO (Actividad 6).
 */
public class TareaDAOMemoria implements TareaDAO {
    private final List<Tarea> datos = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public void insertar(Tarea entidad) {
        entidad.setId(contadorId++);
        datos.add(entidad);
    }

    @Override
    public Tarea buscar(int id) {
        for (Tarea t : datos) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Tarea> listar() {
        return new ArrayList<>(datos);
    }

    @Override
    public boolean actualizar(Tarea nuevo) {
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).getId() == nuevo.getId()) {
                datos.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        return datos.removeIf(t -> t.getId() == id);
    }

    @Override
    public List<Tarea> listarPorMateria(int materiaId) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : datos) {
            if (t.getMateriaId() == materiaId) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public List<Tarea> buscarPorTitulo(String texto) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : datos) {
            if (t.getTitulo() != null && t.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public List<Tarea> buscarPorTitulo(int materiaId, String texto) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : datos) {
            if (t.getMateriaId() == materiaId && t.getTitulo() != null && t.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}
