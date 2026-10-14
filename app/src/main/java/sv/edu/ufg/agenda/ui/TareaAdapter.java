package sv.edu.ufg.agenda.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.Fila> {

    public interface Acciones {
        void editar(Tarea tarea);
        void eliminar(Tarea tarea);
        void alternarCompletada(Tarea tarea, boolean completada);
    }

    private final List<Tarea> datos = new ArrayList<>();
    private final Acciones acciones;

    public TareaAdapter(Acciones acciones) {
        this.acciones = acciones;
    }

    public void mostrar(List<Tarea> nuevas) {
        datos.clear();
        datos.addAll(nuevas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fila onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext())
                .inflate(R.layout.item_tarea, padre, false);
        return new Fila(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Fila fila, int posicion) {
        Tarea t = datos.get(posicion);

        fila.tvTituloTarea.setText(t.getTitulo());
        if (t.isCompletada()) {
            fila.tvTituloTarea.setPaintFlags(fila.tvTituloTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            fila.tvTituloTarea.setTextColor(Color.GRAY);
        } else {
            fila.tvTituloTarea.setPaintFlags(fila.tvTituloTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            fila.tvTituloTarea.setTextColor(Color.BLACK);
        }

        String detalle = "Entrega: " + t.getFechaEntrega() + "  |  Prioridad: " + t.getPrioridad();
        fila.tvDetalleTarea.setText(detalle);

        // Desactivar listener temporalmente para evitar disparos al reciclar vistas
        fila.cbCompletada.setOnCheckedChangeListener(null);
        fila.cbCompletada.setChecked(t.isCompletada());
        fila.cbCompletada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            acciones.alternarCompletada(t, isChecked);
        });

        fila.btnEditarTarea.setOnClickListener(v -> acciones.editar(t));
        fila.btnEliminarTarea.setOnClickListener(v -> acciones.eliminar(t));
        fila.itemView.setOnClickListener(v -> acciones.editar(t));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    static class Fila extends RecyclerView.ViewHolder {
        final CheckBox cbCompletada;
        final TextView tvTituloTarea;
        final TextView tvDetalleTarea;
        final Button btnEditarTarea;
        final Button btnEliminarTarea;

        Fila(@NonNull View vista) {
            super(vista);
            cbCompletada = vista.findViewById(R.id.cbCompletada);
            tvTituloTarea = vista.findViewById(R.id.tvTituloTarea);
            tvDetalleTarea = vista.findViewById(R.id.tvDetalleTarea);
            btnEditarTarea = vista.findViewById(R.id.btnEditarTarea);
            btnEliminarTarea = vista.findViewById(R.id.btnEliminarTarea);
        }
    }
}
