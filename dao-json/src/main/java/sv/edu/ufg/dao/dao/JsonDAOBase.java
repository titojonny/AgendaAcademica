package sv.edu.ufg.dao.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.dao.modelo.Entidad;

/**
 * Clase base genérica que implementa el CRUD y la persistencia en formato JSON.
 * Cumple el reto de reutilización de código (Pág. 25 de la guía): centraliza
 * cargar(), guardar() y siguienteId() para cualquier entidad.
 *
 * @param <T> Tipo de entidad que implementa Entidad
 */
public abstract class JsonDAOBase<T extends Entidad> implements DAO<T> {
    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type tipoLista;

    protected JsonDAOBase(Path archivo, Class<T> clase) {
        this.archivo = archivo;
        this.tipoLista = TypeToken.getParameterized(List.class, clase).getType();
    }

    protected JsonDAOBase(String rutaArchivo, Class<T> clase) {
        this(Path.of(rutaArchivo), clase);
    }

    // ---------- Operaciones del contrato genérico ----------

    @Override
    public void insertar(T entidad) {
        List<T> lista = cargar();
        entidad.setId(siguienteId(lista));
        lista.add(entidad);
        guardar(lista);
    }

    @Override
    public T buscar(int id) {
        for (T e : cargar()) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public List<T> listar() {
        return cargar();
    }

    @Override
    public boolean actualizar(T nuevo) {
        List<T> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == nuevo.getId()) {
                lista.set(i, nuevo);
                guardar(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        List<T> lista = cargar();
        boolean quitado = lista.removeIf(e -> e.getId() == id);
        if (quitado) {
            guardar(lista);
        }
        return quitado;
    }

    // ---------- Métodos reutilizables para clases hijas ----------

    protected List<T> cargar() {
        try {
            if (Files.notExists(archivo)) {
                return new ArrayList<>();
            }
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            List<T> lista = gson.fromJson(json, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo leer " + archivo, ex);
        }
    }

    protected void guardar(List<T> lista) {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            Files.writeString(archivo, gson.toJson(lista, tipoLista), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo escribir " + archivo, ex);
        }
    }

    private int siguienteId(List<T> lista) {
        int mayor = 0;
        for (T e : lista) {
            mayor = Math.max(mayor, e.getId());
        }
        return mayor + 1;
    }
}
