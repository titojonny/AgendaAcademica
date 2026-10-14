package sv.edu.ufg.agenda.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaDAOJsonTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private TareaDAOJson tareaDAO;
    private File archivoTareas;

    @Before
    public void setUp() throws IOException {
        archivoTareas = temporaryFolder.newFile("tareas_test.json");
        if (archivoTareas.exists()) {
            archivoTareas.delete();
        }
        tareaDAO = new TareaDAOJson(archivoTareas);
    }

    @Test
    public void testInsertarYBuscar() {
        Tarea tarea = new Tarea(0, 1, "Guía práctica 3", "2026-10-20", false, "Alta");
        tareaDAO.insertar(tarea);

        assertTrue("El ID debe ser mayor a 0", tarea.getId() > 0);

        Tarea encontrada = tareaDAO.buscar(tarea.getId());
        assertNotNull("La tarea debe existir", encontrada);
        assertEquals("Guía práctica 3", encontrada.getTitulo());
        assertEquals("Alta", encontrada.getPrioridad());
    }

    @Test
    public void testListarPorMateria() {
        tareaDAO.insertar(new Tarea(0, 1, "Tarea 1 Materia 1", "2026-10-21"));
        tareaDAO.insertar(new Tarea(0, 1, "Tarea 2 Materia 1", "2026-10-22"));
        tareaDAO.insertar(new Tarea(0, 2, "Tarea 1 Materia 2", "2026-10-23"));

        List<Tarea> tareasMateria1 = tareaDAO.listarPorMateria(1);
        List<Tarea> tareasMateria2 = tareaDAO.listarPorMateria(2);

        assertEquals(2, tareasMateria1.size());
        assertEquals(1, tareasMateria2.size());
    }

    @Test
    public void testEliminar() {
        Tarea tarea = new Tarea(0, 1, "Tarea a eliminar", "2026-10-25");
        tareaDAO.insertar(tarea);
        int id = tarea.getId();

        assertNotNull(tareaDAO.buscar(id));

        boolean eliminado = tareaDAO.eliminar(id);
        assertTrue("Debe retornar true al eliminar exitosamente", eliminado);
        assertNull("La tarea eliminada ya no debe encontrarse", tareaDAO.buscar(id));
    }
}
