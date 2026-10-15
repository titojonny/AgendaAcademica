package sv.edu.ufg.dao.modelo;

/**
 * Segunda entidad solicitada en las actividades de la Parte I (Pág. 24 de la guía).
 */
public class Curso implements Entidad {
    private int id;
    private String codigo;
    private String nombre;
    private int uv; // Unidades valorativas / créditos

    public Curso() { }

    public Curso(int id, String codigo, String nombre, int uv) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.uv = uv;
    }

    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getUv() { return uv; }
    public void setUv(int uv) { this.uv = uv; }

    @Override
    public String toString() {
        return id + " | " + codigo + " | " + nombre + " (" + uv + " UV)";
    }
}
