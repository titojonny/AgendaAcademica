package sv.edu.ufg.dao.modelo;

public class Estudiante implements Entidad {
    private int id;
    private String nombre;
    private String carrera;
    private String correo; // Atributo adicional para actividades de la práctica

    public Estudiante() { }

    public Estudiante(int id, String nombre, String carrera) {
        this(id, nombre, carrera, "");
    }

    public Estudiante(int id, String nombre, String carrera, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.carrera = carrera;
        this.correo = correo;
    }

    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override
    public String toString() {
        return id + " | " + nombre + " | " + carrera + (correo != null && !correo.isEmpty() ? " | " + correo : "");
    }
}
