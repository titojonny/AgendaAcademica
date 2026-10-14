package sv.edu.ufg.agenda.dao;

import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.modelo.Materia;

/**
 * Implementación en memoria de MateriaDAO (Actividad 6).
 * Permite trabajar y probar la UI sin depender del almacenamiento en archivos.
 */
public class MateriaDAOMemoria implements MateriaDAO {
    private final List<Materia> datos = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public void insertar(Materia entidad) {
        entidad.setId(contadorId++);
        datos.add(entidad);
    }

    @Override
    public Materia buscar(int id) {
        for (Materia m : datos) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Materia> listar() {
        return new ArrayList<>(datos);
    }

    @Override
    public boolean actualizar(Materia nuevo) {
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
        return datos.removeIf(m -> m.getId() == id);
    }

    @Override
    public List<Materia> buscarPorNombre(String texto) {
        List<Materia> resultado = new ArrayList<>();
        for (Materia m : datos) {
            if (m.getNombre() != null && m.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}
