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

import sv.edu.ufg.agenda.modelo.Materia;

public class MateriaDAOJsonTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private MateriaDAOJson materiaDAO;
    private File archivoMaterias;

    @Before
    public void setUp() throws IOException {
        archivoMaterias = temporaryFolder.newFile("materias_test.json");
        if (archivoMaterias.exists()) {
            archivoMaterias.delete();
        }
        materiaDAO = new MateriaDAOJson(archivoMaterias);
    }

    @Test
    public void testInsertarYBuscar() {
        Materia materia = new Materia(0, "Programación II", "Ing. Pérez");
        materiaDAO.insertar(materia);

        assertTrue("El ID generado debe ser mayor a 0", materia.getId() > 0);

        Materia encontrada = materiaDAO.buscar(materia.getId());
        assertNotNull("La materia debe existir en el archivo", encontrada);
        assertEquals("Programación II", encontrada.getNombre());
        assertEquals("Ing. Pérez", encontrada.getDocente());
    }

    @Test
    public void testBuscarPorNombre() {
        materiaDAO.insertar(new Materia(0, "Bases de Datos", "Lic. Gómez"));
        materiaDAO.insertar(new Materia(0, "Programación Web", "Ing. Rodríguez"));
        materiaDAO.insertar(new Materia(0, "Sistemas Operativos", "Ing. Martínez"));

        List<Materia> filtro = materiaDAO.buscarPorNombre("prog");
        assertEquals(1, filtro.size());
        assertEquals("Programación Web", filtro.get(0).getNombre());
    }

    @Test
    public void testActualizarYEliminar() {
        Materia materia = new Materia(0, "Física I", "Lic. Castillo");
        materiaDAO.insertar(materia);
        int id = materia.getId();

        materia.setNombre("Física I - Teórica");
        boolean actualizado = materiaDAO.actualizar(materia);
        assertTrue("Debe retornar true al actualizar", actualizado);

        Materia modificada = materiaDAO.buscar(id);
        assertEquals("Física I - Teórica", modificada.getNombre());

        boolean eliminado = materiaDAO.eliminar(id);
        assertTrue("Debe retornar true al eliminar", eliminado);
        assertNull("La materia eliminada ya no debe existir", materiaDAO.buscar(id));
    }
}
