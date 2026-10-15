package sv.edu.ufg.agenda.ui;

import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

        // Validar materiaId de entrada (Regla 2: una tarea solo existe para una materia válida)
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

        // Selector visual de fecha interactivo (DatePickerDialog con bloqueo de fechas pasadas)
        etFechaEntrega.setFocusable(false);
        etFechaEntrega.setClickable(true);
        etFechaEntrega.setOnClickListener(v -> abrirSelectorFecha());

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

    private void abrirSelectorFecha() {
        Calendar calendario = Calendar.getInstance();

        // Si ya hay una fecha escrita válida, inicializar el selector en esa fecha
        String textoActual = etFechaEntrega.getText().toString().trim();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        if (!textoActual.isEmpty()) {
            try {
                Date parsed = formato.parse(textoActual);
                if (parsed != null) {
                    calendario.setTime(parsed);
                }
            } catch (ParseException ignored) {}
        }

        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            etFechaEntrega.setText(fechaSeleccionada);
            etFechaEntrega.setError(null);
        }, anio, mes, dia);

        // Bloqueo de fechas pasadas en el propio selector de calendario
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();
    }

    private void guardar() {
        String titulo = etTitulo.getText().toString().trim();
        String fecha = etFechaEntrega.getText().toString().trim();
        String prioridad = spPrioridad.getSelectedItem().toString();

        // 1. Regla 1: El título es obligatorio y debe tener al menos 3 caracteres
        if (titulo.isEmpty()) {
            etTitulo.setError("El título de la tarea es obligatorio");
            etTitulo.requestFocus();
            return;
        }
        if (titulo.length() < 3) {
            etTitulo.setError("El título debe tener al menos 3 caracteres");
            etTitulo.requestFocus();
            return;
        }

        // 2. Validación de fecha obligatoria
        if (fecha.isEmpty()) {
            etFechaEntrega.setError("La fecha de entrega es obligatoria");
            etFechaEntrega.requestFocus();
            return;
        }

        // 3. Regla 4: Formato estricto y fecha real de calendario
        if (!fechaValida(fecha)) {
            etFechaEntrega.setError("Fecha inválida. Use formato aaaa-mm-dd (ej. 2026-10-25)");
            etFechaEntrega.requestFocus();
            return;
        }

        // 4. Validación: NO permitir crear o actualizar con fecha pasada
        if (esFechaPasada(fecha)) {
            etFechaEntrega.setError("La fecha de entrega no puede ser anterior a la fecha de hoy");
            etFechaEntrega.requestFocus();
            return;
        }

        // 5. Validación de tareas duplicadas en la misma materia
        for (Tarea t : tareaDAO.listarPorMateria(materiaId)) {
            if (t.getTitulo().equalsIgnoreCase(titulo)) {
                if (enEdicion == null || t.getId() != enEdicion.getId()) {
                    etTitulo.setError("Ya existe una tarea con este título en la materia");
                    etTitulo.requestFocus();
                    return;
                }
            }
        }

        if (enEdicion == null) {
            Tarea nueva = new Tarea(0, materiaId, titulo, fecha, false, prioridad);
            tareaDAO.insertar(nueva);
            Toast.makeText(this, "Tarea agregada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            enEdicion.setTitulo(titulo);
            enEdicion.setFechaEntrega(fecha);
            enEdicion.setPrioridad(prioridad);
            tareaDAO.actualizar(enEdicion);
            Toast.makeText(this, "Tarea actualizada con éxito", Toast.LENGTH_SHORT).show();
        }

        limpiarFormulario();
        refrescar();
    }

    private void limpiarFormulario() {
        enEdicion = null;
        etTitulo.setText("");
        etFechaEntrega.setText("");
        etTitulo.setError(null);
        etFechaEntrega.setError(null);
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

        // Ordenar cronológicamente por fecha de entrega
        Collections.sort(tareas, (a, b) -> a.getFechaEntrega().compareTo(b.getFechaEntrega()));

        // Encabezado dinámico: "X de Y completadas"
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

    private boolean esFechaPasada(String texto) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formato.setLenient(false);
        try {
            Date fechaEntrega = formato.parse(texto.trim());
            if (fechaEntrega == null) return true;

            // Medianoche de hoy (00:00:00.000)
            Calendar hoy = Calendar.getInstance();
            hoy.set(Calendar.HOUR_OF_DAY, 0);
            hoy.set(Calendar.MINUTE, 0);
            hoy.set(Calendar.SECOND, 0);
            hoy.set(Calendar.MILLISECOND, 0);

            return fechaEntrega.before(hoy.getTime());
        } catch (ParseException ex) {
            return true;
        }
    }

    @Override
    public void editar(Tarea tarea) {
        enEdicion = tarea;
        etTitulo.setText(tarea.getTitulo());
        etFechaEntrega.setText(tarea.getFechaEntrega());
        etTitulo.setError(null);
        etFechaEntrega.setError(null);

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
        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
        refrescar();
    }

    @Override
    public void alternarCompletada(Tarea tarea, boolean completada) {
        tarea.setCompletada(completada);
        tareaDAO.actualizar(tarea);
        refrescar();
    }
}
