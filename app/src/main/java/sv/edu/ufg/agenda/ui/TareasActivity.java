package sv.edu.ufg.agenda.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareasActivity extends AppCompatActivity implements TareaAdapter.Acciones {

    private MateriaDAO materiaDAO;
    private TareaDAO tareaDAO;
    private TareaAdapter adapter;

    private int materiaId;
    private Materia materia;

    private TextView tvNombreMateria;
    private TextView tvResumenTareas;
    private EditText etBuscarTarea;
    private EditText etTitulo;
    private EditText etFechaEntrega;
    private Spinner spPrioridad;
    private Button btnGuardarTarea;

    private Tarea enEdicion; // null = creando tarea nueva
    private final String[] opcionesPrioridad = new String[]{"Alta", "Media", "Baja"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        DAOFactory daos = ((AgendaApp) getApplication()).daos();
        materiaDAO = daos.materias();
        tareaDAO = daos.tareas();

        // Validar materiaId de entrada (Regla 2 e integridad)
        materiaId = getIntent().getIntExtra("materiaId", -1);
        materia = materiaDAO.buscar(materiaId);
        if (materia == null) {
            Toast.makeText(this, "Materia no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvNombreMateria = findViewById(R.id.tvNombreMateria);
        tvResumenTareas = findViewById(R.id.tvResumenTareas);
        etBuscarTarea = findViewById(R.id.etBuscarTarea);
        etTitulo = findViewById(R.id.etTitulo);
        etFechaEntrega = findViewById(R.id.etFechaEntrega);
        spPrioridad = findViewById(R.id.spPrioridad);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);

        tvNombreMateria.setText(materia.getNombre());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, opcionesPrioridad);
        spPrioridad.setAdapter(spinnerAdapter);
        spPrioridad.setSelection(1); // "Media" por defecto

        adapter = new TareaAdapter(this);
        RecyclerView lista = findViewById(R.id.rvTareas);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        btnGuardarTarea.setOnClickListener(v -> guardar());

        // Búsqueda en tiempo real por título (Actividad 4)
        etBuscarTarea.addTextChangedListener(new TextWatcher() {
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

    private void guardar() {
        String titulo = etTitulo.getText().toString().trim();
        String fecha = etFechaEntrega.getText().toString().trim();
        String prioridad = spPrioridad.getSelectedItem().toString();

        // Regla 1: El título es obligatorio
        if (titulo.isEmpty()) {
            etTitulo.setError("El título es obligatorio");
            return;
        }

        // Regla 4: Fecha de entrega válida con formato aaaa-mm-dd
        if (!fechaValida(fecha)) {
            etFechaEntrega.setError("Fecha inválida. Use formato aaaa-mm-dd real (ej. 2026-10-20)");
            return;
        }

        if (enEdicion == null) {
            Tarea nueva = new Tarea(0, materiaId, titulo, fecha, false, prioridad);
            tareaDAO.insertar(nueva);
        } else {
            enEdicion.setTitulo(titulo);
            enEdicion.setFechaEntrega(fecha);
            enEdicion.setPrioridad(prioridad);
            tareaDAO.actualizar(enEdicion);
        }

        limpiarFormulario();
        refrescar();
    }

    private void limpiarFormulario() {
        enEdicion = null;
        etTitulo.setText("");
        etFechaEntrega.setText("");
        spPrioridad.setSelection(1);
        btnGuardarTarea.setText("Agregar tarea");
    }

    private void refrescar() {
        String filtro = etBuscarTarea.getText().toString().trim();
        List<Tarea> tareas;
        if (!filtro.isEmpty()) {
            tareas = tareaDAO.buscarPorTitulo(materiaId, filtro);
        } else {
            tareas = tareaDAO.listarPorMateria(materiaId);
        }

        // Ordenar por fecha de entrega (Paso 7)
        Collections.sort(tareas, (a, b) -> a.getFechaEntrega().compareTo(b.getFechaEntrega()));

        // Encabezado: "X de Y completadas"
        List<Tarea> todasDeMateria = tareaDAO.listarPorMateria(materiaId);
        int total = todasDeMateria.size();
        int completadas = 0;
        for (Tarea t : todasDeMateria) {
            if (t.isCompletada()) {
                completadas++;
            }
        }
        tvResumenTareas.setText(completadas + " de " + total + " completadas");

        adapter.mostrar(tareas);
    }

    private boolean fechaValida(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formato.setLenient(false);
        try {
            formato.parse(texto.trim());
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    @Override
    public void editar(Tarea tarea) {
        enEdicion = tarea;
        etTitulo.setText(tarea.getTitulo());
        etFechaEntrega.setText(tarea.getFechaEntrega());

        // Seleccionar la prioridad en el Spinner
        for (int i = 0; i < opcionesPrioridad.length; i++) {
            if (opcionesPrioridad[i].equalsIgnoreCase(tarea.getPrioridad())) {
                spPrioridad.setSelection(i);
                break;
            }
        }
        btnGuardarTarea.setText("Actualizar tarea");
    }

    @Override
    public void eliminar(Tarea tarea) {
        tareaDAO.eliminar(tarea.getId());
        if (enEdicion != null && enEdicion.getId() == tarea.getId()) {
            limpiarFormulario();
        }
        refrescar();
    }

    @Override
    public void alternarCompletada(Tarea tarea, boolean completada) {
        tarea.setCompletada(completada);
        tareaDAO.actualizar(tarea);
        refrescar();
    }
}
