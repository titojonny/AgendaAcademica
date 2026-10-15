package sv.edu.ufg.dao;

import java.util.List;
import java.util.Scanner;
import sv.edu.ufg.dao.dao.CursoDAO;
import sv.edu.ufg.dao.dao.DAOFactory;
import sv.edu.ufg.dao.dao.EstudianteDAO;
import sv.edu.ufg.dao.modelo.Curso;
import sv.edu.ufg.dao.modelo.Estudiante;

public class Main {
    public static void main(String[] args) {
        // Se utiliza la fábrica para obtener los DAOs; el cliente depende exclusivamente de las interfaces
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

        // --- 1. Demostración Entidad Estudiante (Pasos 1 al 5 + Actividades) ---
        System.out.println("\n>>> [A] PRUEBAS DE LA ENTIDAD ESTUDIANTE <<<");
        System.out.println("\n[1] Insertando estudiantes iniciales...");
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

        // --- 2. Demostración Segunda Entidad Curso (Actividad Pág. 24) ---
        System.out.println("\n>>> [B] PRUEBAS DE LA SEGUNDA ENTIDAD: CURSO (Actividad Pág. 24) <<<");
        System.out.println("\n[1] Insertando cursos iniciales...");
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
        System.out.println("Tip: Ejecuta con el argumento '--menu' para el menú interactivo completo.");
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
                System.out.println("Opción inválida.");
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
                    System.out.println("Opción no válida.");
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
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine().trim();
                    System.out.print("Carrera: ");
                    String carrera = scanner.nextLine().trim();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine().trim();
                    dao.insertar(new Estudiante(0, nombre, carrera, correo));
                    System.out.println("Estudiante insertado exitosamente.");
                    break;
                case 2:
                    System.out.println("\nLista de estudiantes:");
                    List<Estudiante> lista = dao.listar();
                    if (lista.isEmpty()) System.out.println("(No hay registros)");
                    else lista.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("ID a buscar: ");
                    int idBuscar = Integer.parseInt(scanner.nextLine().trim());
                    Estudiante encontrado = dao.buscar(idBuscar);
                    System.out.println(encontrado != null ? "Encontrado: " + encontrado : "No se encontró ID " + idBuscar);
                    break;
                case 4:
                    System.out.print("ID a actualizar: ");
                    int idAct = Integer.parseInt(scanner.nextLine().trim());
                    Estudiante estAct = dao.buscar(idAct);
                    if (estAct != null) {
                        System.out.print("Nuevo nombre (Enter para mantener '" + estAct.getNombre() + "'): ");
                        String nNombre = scanner.nextLine().trim();
                        if (!nNombre.isEmpty()) estAct.setNombre(nNombre);

                        System.out.print("Nueva carrera (Enter para mantener '" + estAct.getCarrera() + "'): ");
                        String nCarrera = scanner.nextLine().trim();
                        if (!nCarrera.isEmpty()) estAct.setCarrera(nCarrera);

                        System.out.print("Nuevo correo (Enter para mantener '" + estAct.getCorreo() + "'): ");
                        String nCorreo = scanner.nextLine().trim();
                        if (!nCorreo.isEmpty()) estAct.setCorreo(nCorreo);

                        dao.actualizar(estAct);
                        System.out.println("Estudiante actualizado correctamente.");
                    } else {
                        System.out.println("Estudiante no encontrado.");
                    }
                    break;
                case 5:
                    System.out.print("ID a eliminar: ");
                    int idElim = Integer.parseInt(scanner.nextLine().trim());
                    System.out.println(dao.eliminar(idElim) ? "Eliminado correctamente." : "No se encontró el ID.");
                    break;
                case 6:
                    System.out.print("Carrera: ");
                    String carr = scanner.nextLine().trim();
                    List<Estudiante> filtrados = dao.buscarPorCarrera(carr);
                    if (filtrados.isEmpty()) System.out.println("(No hay estudiantes en esa carrera)");
                    else filtrados.forEach(System.out::println);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
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
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Código: ");
                    String codigo = scanner.nextLine().trim();
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine().trim();
                    System.out.print("Unidades Valorativas (UV): ");
                    int uv = Integer.parseInt(scanner.nextLine().trim());
                    dao.insertar(new Curso(0, codigo, nombre, uv));
                    System.out.println("Curso insertado exitosamente.");
                    break;
                case 2:
                    System.out.println("\nLista de cursos:");
                    List<Curso> lista = dao.listar();
                    if (lista.isEmpty()) System.out.println("(No hay cursos registrados)");
                    else lista.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("ID a buscar: ");
                    int idBuscar = Integer.parseInt(scanner.nextLine().trim());
                    Curso encontrado = dao.buscar(idBuscar);
                    System.out.println(encontrado != null ? "Encontrado: " + encontrado : "No se encontró ID " + idBuscar);
                    break;
                case 4:
                    System.out.print("ID a actualizar: ");
                    int idAct = Integer.parseInt(scanner.nextLine().trim());
                    Curso cursoAct = dao.buscar(idAct);
                    if (cursoAct != null) {
                        System.out.print("Nuevo código (Enter para mantener '" + cursoAct.getCodigo() + "'): ");
                        String nCod = scanner.nextLine().trim();
                        if (!nCod.isEmpty()) cursoAct.setCodigo(nCod);

                        System.out.print("Nuevo nombre (Enter para mantener '" + cursoAct.getNombre() + "'): ");
                        String nNom = scanner.nextLine().trim();
                        if (!nNom.isEmpty()) cursoAct.setNombre(nNom);

                        System.out.print("Nuevas UV (Enter para mantener '" + cursoAct.getUv() + "'): ");
                        String nUv = scanner.nextLine().trim();
                        if (!nUv.isEmpty()) cursoAct.setUv(Integer.parseInt(nUv));

                        dao.actualizar(cursoAct);
                        System.out.println("Curso actualizado correctamente.");
                    } else {
                        System.out.println("Curso no encontrado.");
                    }
                    break;
                case 5:
                    System.out.print("ID a eliminar: ");
                    int idElim = Integer.parseInt(scanner.nextLine().trim());
                    System.out.println(dao.eliminar(idElim) ? "Curso eliminado correctamente." : "No se encontró el ID.");
                    break;
                case 6:
                    System.out.print("Texto a buscar en el nombre: ");
                    String texto = scanner.nextLine().trim();
                    List<Curso> filtrados = dao.buscarPorNombre(texto);
                    if (filtrados.isEmpty()) System.out.println("(No se encontraron cursos con ese nombre)");
                    else filtrados.forEach(System.out::println);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
