package sv.edu.ufg.agenda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;
import sv.edu.ufg.agenda.modelo.Tarea;

public class MateriasActivity extends AppCompatActivity implements MateriaAdapter.Acciones {

    private MateriaDAO materiaDAO;
    private TareaDAO tareaDAO;
    private MateriaAdapter adapter;
    private EditText etNombre;
    private EditText etDocente;
    private EditText etBuscarMateria;
    private Button btnGuardar;
    private Materia enEdicion; // null = se está agregando una materia nueva

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);

        DAOFactory daos = ((AgendaApp) getApplication()).daos();
        materiaDAO = daos.materias();
        tareaDAO = daos.tareas();

        etBuscarMateria = findViewById(R.id.etBuscarMateria);
        etNombre = findViewById(R.id.etNombre);
        etDocente = findViewById(R.id.etDocente);
        btnGuardar = findViewById(R.id.btnGuardar);

        adapter = new MateriaAdapter(this);
        RecyclerView lista = findViewById(R.id.rvMaterias);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardar());

        // Búsqueda en tiempo real por nombre (Actividad 4)
        etBuscarMateria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refrescar();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        refrescar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refrescar();
    }

    private void guardar() {
        String nombre = etNombre.getText().toString().trim();
        String docente = etDocente.getText().toString().trim();

        // 1. Validación de nombre obligatorio y longitud mínima
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre de la materia es obligatorio");
            etNombre.requestFocus();
            return;
        }
        if (nombre.length() < 3) {
            etNombre.setError("El nombre debe tener al menos 3 caracteres");
            etNombre.requestFocus();
            return;
        }

        // 2. Validación de docente obligatorio y longitud mínima
        if (docente.isEmpty()) {
            etDocente.setError("El nombre del docente es obligatorio");
            etDocente.requestFocus();
            return;
        }
        if (docente.length() < 3) {
            etDocente.setError("El nombre del docente debe tener al menos 3 caracteres");
            etDocente.requestFocus();
            return;
        }

        // 3. Validación de duplicados (no permitir materias con el mismo nombre)
        for (Materia m : materiaDAO.listar()) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                if (enEdicion == null || m.getId() != enEdicion.getId()) {
                    etNombre.setError("Ya existe una materia registrada con este nombre");
                    etNombre.requestFocus();
                    return;
                }
            }
        }

        if (enEdicion == null) {
            materiaDAO.insertar(new Materia(0, nombre, docente));
            Toast.makeText(this, "Materia agregada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            enEdicion.setNombre(nombre);
            enEdicion.setDocente(docente);
            materiaDAO.actualizar(enEdicion);
            Toast.makeText(this, "Materia actualizada con éxito", Toast.LENGTH_SHORT).show();
        }

        limpiarFormulario();
        refrescar();
    }

    private void limpiarFormulario() {
        enEdicion = null;
        etNombre.setText("");
        etDocente.setText("");
        etNombre.setError(null);
        etDocente.setError(null);
        btnGuardar.setText("Agregar materia");
    }

    private void refrescar() {
        String filtro = etBuscarMateria.getText().toString().trim();
        List<Materia> materias;
        if (!filtro.isEmpty()) {
            materias = materiaDAO.buscarPorNombre(filtro);
        } else {
            materias = materiaDAO.listar();
        }

        // Resumen por materia: cálculo de tareas pendientes (Actividad 5)
        Map<Integer, Integer> mapaPendientes = new HashMap<>();
        for (Materia m : materias) {
            int pendientes = 0;
            for (Tarea t : tareaDAO.listarPorMateria(m.getId())) {
                if (!t.isCompletada()) {
                    pendientes++;
                }
            }
            mapaPendientes.put(m.getId(), pendientes);
        }

        adapter.mostrar(materias, mapaPendientes);
    }

    @Override
    public void editar(Materia materia) {
        enEdicion = materia;
        etNombre.setText(materia.getNombre());
        etDocente.setText(materia.getDocente());
        etNombre.setError(null);
        etDocente.setError(null);
        btnGuardar.setText("Actualizar materia");
    }

    @Override
    public void verTareas(Materia materia) {
        Intent intento = new Intent(this, TareasActivity.class);
        intento.putExtra("materiaId", materia.getId());
        startActivity(intento);
    }

    @Override
    public void eliminar(Materia materia) {
        if (!tareaDAO.listarPorMateria(materia.getId()).isEmpty()) {
            Toast.makeText(this, "No se puede eliminar: la materia tiene tareas pendientes o asignadas",
                    Toast.LENGTH_LONG).show();
            return;
        }

        materiaDAO.eliminar(materia.getId());
        if (enEdicion != null && enEdicion.getId() == materia.getId()) {
            limpiarFormulario();
        }
        Toast.makeText(this, "Materia eliminada", Toast.LENGTH_SHORT).show();
        refrescar();
    }
}
