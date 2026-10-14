package sv.edu.ufg.agenda.modelo;

public class Tarea implements Entidad {
    private int id;
    private int materiaId;
    private String titulo;
    private String fechaEntrega; // texto con formato aaaa-mm-dd
    private boolean completada;
    private String prioridad; // "Alta", "Media", "Baja" (Actividad 3)

    public Tarea() { }

    public Tarea(int id, int materiaId, String titulo, String fechaEntrega) {
        this(id, materiaId, titulo, fechaEntrega, false, "Media");
    }

    public Tarea(int id, int materiaId, String titulo, String fechaEntrega, boolean completada, String prioridad) {
        this.id = id;
        this.materiaId = materiaId;
        this.titulo = titulo;
        this.fechaEntrega = fechaEntrega;
        this.completada = completada;
        this.prioridad = (prioridad != null && !prioridad.trim().isEmpty()) ? prioridad : "Media";
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
}
