package sv.edu.ufg.dao;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import sv.edu.ufg.dao.dao.CursoDAO;
import sv.edu.ufg.dao.dao.DAOFactory;
import sv.edu.ufg.dao.dao.EstudianteDAO;
import sv.edu.ufg.dao.modelo.Curso;
import sv.edu.ufg.dao.modelo.Estudiante;

public class Main {

    private static final Pattern PATRON_EMAIL = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public static void main(String[] args) {
        DAOFactory factory = new DAOFactory("datos");
        EstudianteDAO estudianteDAO = factory.estudiantes();
        CursoDAO cursoDAO = factory.cursos();

        if (args.length > 0 && args[0].equalsIgnoreCase("--menu")) {
            ejecutarMenu(estudianteDAO, cursoDAO);
            return;
        }

        System.out.println("=================================================================");
        System.out.println("   DEMOSTRACION PARTE I: PATRON DAO CON ARCHIVO JSON (CONSOLA)");
        System.out.println("=================================================================");

        // --- 1. Demostración Entidad Estudiante ---
        System.out.println("\n>>> [A] PRUEBAS DE LA ENTIDAD ESTUDIANTE <<<");
        System.out.println("\n[1] Insertando estudiantes iniciales con datos validados...");
        estudianteDAO.insertar(new Estudiante(0, "Ana Martínez", "Ingeniería en Sistemas", "ana.martinez@ufg.edu.sv"));
        estudianteDAO.insertar(new Estudiante(0, "Luis Pérez", "Ingeniería en Sistemas", "luis.perez@ufg.edu.sv"));

        System.out.println("\n--- Listado de Estudiantes ---");
        estudianteDAO.listar().forEach(System.out::println);

        System.out.println("\n[2] Buscando estudiante con ID 2 y actualizando carrera...");
        Estudiante e = estudianteDAO.buscar(2);
        if (e != null) {
            e.setCarrera("Ingeniería Industrial");
            estudianteDAO.actualizar(e);
            System.out.println("Actualizado: " + e);
        }

        System.out.println("\n[3] Eliminando estudiante con ID 1...");
        estudianteDAO.eliminar(1);

        System.out.println("\n--- Después de actualizar y eliminar ---");
        estudianteDAO.listar().forEach(System.out::println);

        System.out.println("\n[4] Prueba de búsqueda por carrera ('Ingeniería Industrial'):");
        List<Estudiante> industriales = estudianteDAO.buscarPorCarrera("Ingeniería Industrial");
        industriales.forEach(System.out::println);

        // --- 2. Demostración Segunda Entidad Curso ---
        System.out.println("\n>>> [B] PRUEBAS DE LA SEGUNDA ENTIDAD: CURSO (Actividad Pág. 24) <<<");
        System.out.println("\n[1] Insertando cursos iniciales validados...");
        cursoDAO.insertar(new Curso(0, "DAP101", "Desarrollo de Aplicaciones Multiplataforma", 4));
        cursoDAO.insertar(new Curso(0, "BD201", "Bases de Datos Avanzadas", 4));
        cursoDAO.insertar(new Curso(0, "RED301", "Redes de Computadoras", 3));

        System.out.println("\n--- Listado de Cursos ---");
        cursoDAO.listar().forEach(System.out::println);

        System.out.println("\n[2] Buscando curso con ID 2 y actualizando UV...");
        Curso c = cursoDAO.buscar(2);
        if (c != null) {
            c.setUv(5);
            cursoDAO.actualizar(c);
            System.out.println("Actualizado: " + c);
        }

        System.out.println("\n[3] Eliminando curso con ID 3 ('RED301')...");
        cursoDAO.eliminar(3);

        System.out.println("\n--- Cursos después de actualizar y eliminar ---");
        cursoDAO.listar().forEach(System.out::println);

        System.out.println("\n[4] Prueba de búsqueda de curso por nombre ('Multiplataforma'):");
        List<Curso> encontrados = cursoDAO.buscarPorNombre("Multiplataforma");
        encontrados.forEach(System.out::println);

        System.out.println("\n=================================================================");
        System.out.println("Ejecución completada exitosamente.");
        System.out.println("Archivos persistidos: 'datos/estudiantes.json' y 'datos/cursos.json'.");
        System.out.println("Tip: Ejecuta con el argumento '--menu' para el menú interactivo con validaciones.");
        System.out.println("=================================================================");
    }

    private static void ejecutarMenu(EstudianteDAO estudianteDAO, CursoDAO cursoDAO) {
        Scanner scanner = new Scanner(System.in);
        int principal = -1;

        while (principal != 0) {
            System.out.println("\n==========================================");
            System.out.println("      SISTEMA ACADEMICO (PATRON DAO)      ");
            System.out.println("==========================================");
            System.out.println("1. Gestionar Estudiantes");
            System.out.println("2. Gestionar Cursos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                principal = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception ex) {
                System.out.println(" [!] Opción inválida. Ingrese un número.");
                continue;
            }

            switch (principal) {
                case 1:
                    menuEstudiantes(scanner, estudianteDAO);
                    break;
                case 2:
                    menuCursos(scanner, cursoDAO);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println(" [!] Opción no válida.");
            }
        }
    }

    private static void menuEstudiantes(Scanner scanner, EstudianteDAO dao) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE ESTUDIANTES ---");
            System.out.println("1. Insertar estudiante");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar estudiante");
            System.out.println("5. Eliminar estudiante");
            System.out.println("6. Buscar por carrera");
            System.out.println("0. Regresar al menú principal");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception ex) {
                System.out.println(" [!] Ingrese una opción numérica válida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n[Registrar Nuevo Estudiante]");
                    String nombre = solicitarTextoNoVacio(scanner, "Nombre completo", 3);
                    String carrera = solicitarTextoNoVacio(scanner, "Carrera universitaria", 3);
                    String correo = solicitarCorreoValido(scanner);

                    // Validación de duplicado por correo
                    boolean correoDuplicado = false;
                    for (Estudiante est : dao.listar()) {
                        if (est.getCorreo() != null && est.getCorreo().equalsIgnoreCase(correo)) {
                            correoDuplicado = true;
                            break;
                        }
                    }
                    if (correoDuplicado) {
                        System.out.println(" [!] Error: Ya existe un estudiante registrado con el correo: " + correo);
                        break;
                    }

                    dao.insertar(new Estudiante(0, nombre, carrera, correo));
                    System.out.println(" [✓] Estudiante insertado exitosamente y persistido en JSON.");
                    break;

                case 2:
                    System.out.println("\n--- Lista de Estudiantes Registrados ---");
                    List<Estudiante> lista = dao.listar();
                    if (lista.isEmpty()) {
                        System.out.println("(No hay estudiantes registrados en datos/estudiantes.json)");
                    } else {
                        lista.forEach(System.out::println);
                    }
                    break;

                case 3:
                    int idBuscar = solicitarEnteroPositivo(scanner, "Ingrese ID a buscar: ");
                    Estudiante encontrado = dao.buscar(idBuscar);
                    if (encontrado != null) {
                        System.out.println(" [✓] Encontrado: " + encontrado);
                    } else {
                        System.out.println(" [!] No se encontró ningún estudiante con ID: " + idBuscar);
                    }
                    break;

                case 4:
                    int idAct = solicitarEnteroPositivo(scanner, "Ingrese ID a actualizar: ");
                    Estudiante estAct = dao.buscar(idAct);
                    if (estAct == null) {
                        System.out.println(" [!] Estudiante con ID " + idAct + " no existe.");
                        break;
                    }

                    System.out.println("Editando estudiante: " + estAct);
                    System.out.print("Nuevo nombre (Enter para mantener '" + estAct.getNombre() + "'): ");
                    String nNombre = scanner.nextLine().trim();
                    if (!nNombre.isEmpty()) {
                        if (nNombre.length() < 3) {
                            System.out.println(" [!] Nombre muy corto. Se mantiene el anterior.");
                        } else {
                            estAct.setNombre(nNombre);
                        }
                    }

                    System.out.print("Nueva carrera (Enter para mantener '" + estAct.getCarrera() + "'): ");
                    String nCarrera = scanner.nextLine().trim();
                    if (!nCarrera.isEmpty()) {
                        if (nCarrera.length() < 3) {
                            System.out.println(" [!] Carrera muy corta. Se mantiene la anterior.");
                        } else {
                            estAct.setCarrera(nCarrera);
                        }
                    }

                    System.out.print("Nuevo correo (Enter para mantener '" + estAct.getCorreo() + "'): ");
                    String nCorreo = scanner.nextLine().trim();
                    if (!nCorreo.isEmpty()) {
                        if (!esCorreoValido(nCorreo)) {
                            System.out.println(" [!] Formato de correo inválido. Se mantiene el anterior.");
                        } else {
                            estAct.setCorreo(nCorreo);
                        }
                    }

                    dao.actualizar(estAct);
                    System.out.println(" [✓] Estudiante actualizado correctamente.");
                    break;

                case 5:
                    int idElim = solicitarEnteroPositivo(scanner, "Ingrese ID a eliminar: ");
                    if (dao.eliminar(idElim)) {
                        System.out.println(" [✓] Estudiante eliminado correctamente del archivo JSON.");
                    } else {
                        System.out.println(" [!] No se encontró estudiante con ID: " + idElim);
                    }
                    break;

                case 6:
                    String carr = solicitarTextoNoVacio(scanner, "Carrera a filtrar", 2);
                    List<Estudiante> filtrados = dao.buscarPorCarrera(carr);
                    if (filtrados.isEmpty()) {
                        System.out.println("(No se encontraron estudiantes en la carrera: " + carr + ")");
                    } else {
                        System.out.println("\nResultados para '" + carr + "':");
                        filtrados.forEach(System.out::println);
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println(" [!] Opción no válida.");
            }
        }
    }

    private static void menuCursos(Scanner scanner, CursoDAO dao) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE CURSOS ---");
            System.out.println("1. Insertar curso");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar curso");
            System.out.println("5. Eliminar curso");
            System.out.println("6. Buscar por nombre");
            System.out.println("0. Regresar al menú principal");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception ex) {
                System.out.println(" [!] Ingrese una opción numérica válida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n[Registrar Nuevo Curso]");
                    String codigo = solicitarTextoNoVacio(scanner, "Código de curso (ej. DAP101)", 3);
                    String nombre = solicitarTextoNoVacio(scanner, "Nombre de la materia", 3);
                    int uv = solicitarEnteroRango(scanner, "Unidades Valorativas (UV) [1 - 10]: ", 1, 10);

                    // Validación de código duplicado
                    boolean codigoDuplicado = false;
                    for (Curso c : dao.listar()) {
                        if (c.getCodigo() != null && c.getCodigo().equalsIgnoreCase(codigo)) {
                            codigoDuplicado = true;
                            break;
                        }
                    }
                    if (codigoDuplicado) {
                        System.out.println(" [!] Error: Ya existe un curso con el código: " + codigo);
                        break;
                    }

                    dao.insertar(new Curso(0, codigo, nombre, uv));
                    System.out.println(" [✓] Curso insertado exitosamente y persistido en JSON.");
                    break;

                case 2:
                    System.out.println("\n--- Lista de Cursos Registrados ---");
                    List<Curso> lista = dao.listar();
                    if (lista.isEmpty()) {
                        System.out.println("(No hay cursos registrados en datos/cursos.json)");
                    } else {
                        lista.forEach(System.out::println);
                    }
                    break;

                case 3:
                    int idBuscar = solicitarEnteroPositivo(scanner, "Ingrese ID a buscar: ");
                    Curso encontrado = dao.buscar(idBuscar);
                    if (encontrado != null) {
                        System.out.println(" [✓] Encontrado: " + encontrado);
                    } else {
                        System.out.println(" [!] No se encontró ningún curso con ID: " + idBuscar);
                    }
                    break;

                case 4:
                    int idAct = solicitarEnteroPositivo(scanner, "Ingrese ID a actualizar: ");
                    Curso cursoAct = dao.buscar(idAct);
                    if (cursoAct == null) {
                        System.out.println(" [!] Curso con ID " + idAct + " no existe.");
                        break;
                    }

                    System.out.println("Editando curso: " + cursoAct);
                    System.out.print("Nuevo código (Enter para mantener '" + cursoAct.getCodigo() + "'): ");
                    String nCod = scanner.nextLine().trim();
                    if (!nCod.isEmpty()) {
                        if (nCod.length() < 3) {
                            System.out.println(" [!] Código muy corto. Se mantiene el anterior.");
                        } else {
                            cursoAct.setCodigo(nCod);
                        }
                    }

                    System.out.print("Nuevo nombre (Enter para mantener '" + cursoAct.getNombre() + "'): ");
                    String nNom = scanner.nextLine().trim();
                    if (!nNom.isEmpty()) {
                        if (nNom.length() < 3) {
                            System.out.println(" [!] Nombre muy corto. Se mantiene el anterior.");
                        } else {
                            cursoAct.setNombre(nNom);
                        }
                    }

                    System.out.print("Nuevas UV (Enter para mantener '" + cursoAct.getUv() + "'): ");
                    String nUv = scanner.nextLine().trim();
                    if (!nUv.isEmpty()) {
                        try {
                            int uvParsed = Integer.parseInt(nUv);
                            if (uvParsed >= 1 && uvParsed <= 10) {
                                cursoAct.setUv(uvParsed);
                            } else {
                                System.out.println(" [!] UV deben estar entre 1 y 10. Se mantiene el valor anterior.");
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println(" [!] Número de UV inválido. Se mantiene el anterior.");
                        }
                    }

                    dao.actualizar(cursoAct);
                    System.out.println(" [✓] Curso actualizado correctamente.");
                    break;

                case 5:
                    int idElim = solicitarEnteroPositivo(scanner, "Ingrese ID a eliminar: ");
                    if (dao.eliminar(idElim)) {
                        System.out.println(" [✓] Curso eliminado correctamente del archivo JSON.");
                    } else {
                        System.out.println(" [!] No se encontró curso con ID: " + idElim);
                    }
                    break;

                case 6:
                    String texto = solicitarTextoNoVacio(scanner, "Texto a buscar en el nombre", 2);
                    List<Curso> filtrados = dao.buscarPorNombre(texto);
                    if (filtrados.isEmpty()) {
                        System.out.println("(No se encontraron cursos coincidentes con: " + texto + ")");
                    } else {
                        System.out.println("\nResultados para '" + texto + "':");
                        filtrados.forEach(System.out::println);
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println(" [!] Opción no válida.");
            }
        }
    }

    // ==========================================
    // MÉTODOS DE VALIDACIÓN AUXILIARES ROBUSTOS
    // ==========================================

    private static String solicitarTextoNoVacio(Scanner scanner, String etiqueta, int minLongitud) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("  [!] Error: El campo no puede estar vacío.");
                continue;
            }
            if (valor.length() < minLongitud) {
                System.out.println("  [!] Error: Debe contener al menos " + minLongitud + " caracteres.");
                continue;
            }
            return valor;
        }
    }

    private static String solicitarCorreoValido(Scanner scanner) {
        while (true) {
            System.out.print("Correo electrónico: ");
            String correo = scanner.nextLine().trim();
            if (correo.isEmpty()) {
                System.out.println("  [!] Error: El correo electrónico es obligatorio.");
                continue;
            }
            if (!esCorreoValido(correo)) {
                System.out.println("  [!] Error: Formato de correo inválido (ej. usuario@dominio.edu.sv o alumno@gmail.com).");
                continue;
            }
            return correo;
        }
    }

    public static boolean esCorreoValido(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        return PATRON_EMAIL.matcher(correo.trim()).matches();
    }

    private static int solicitarEnteroPositivo(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor <= 0) {
                    System.out.println("  [!] Error: El ID debe ser un número entero mayor a 0.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException ex) {
                System.out.println("  [!] Error: Ingrese un número entero válido.");
            }
        }
    }

    private static int solicitarEnteroRango(Scanner scanner, String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor < min || valor > max) {
                    System.out.println("  [!] Error: El valor debe estar entre " + min + " y " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException ex) {
                System.out.println("  [!] Error: Debe ingresar un valor numérico entero.");
            }
        }
    }
}
