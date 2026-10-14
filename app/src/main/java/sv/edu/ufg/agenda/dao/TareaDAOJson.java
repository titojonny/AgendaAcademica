package sv.edu.ufg.agenda.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaDAOJson extends JsonDAOBase<Tarea> implements TareaDAO {
    public TareaDAOJson(File archivo) {
        super(archivo, Tarea.class);
    }

    @Override
    public List<Tarea> listarPorMateria(int materiaId) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : cargar()) {
            if (t.getMateriaId() == materiaId) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public List<Tarea> buscarPorTitulo(String texto) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : cargar()) {
            if (t.getTitulo() != null && t.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public List<Tarea> buscarPorTitulo(int materiaId, String texto) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : cargar()) {
            if (t.getMateriaId() == materiaId && t.getTitulo() != null && t.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}
