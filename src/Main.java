import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Main {
    static List<Medico> medicos = new ArrayList<>();
    static List<Paciente> pacientes = new ArrayList<>();
    static List<Cita> citas = new ArrayList<>();
    static List<Admin> admins = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        verificarArchivos(); 
        cargarMedicosDesdeArchivo();
        cargarPacientesDesdeArchivo();
        cargarCitasDesdeArchivo();

        admins.add(new Admin("Admin", "1589"));

        System.out.println("\n ----- Sistema de Administración de Citas -----");
        if (login()) {
            menuPrincipal();
        } else {
            System.out.println("Acceso denegado! Saliendo del sistema...");
        }
    }

    public static void verificarArchivos() {
        verificarYCrearArchivo("db/medicos.csv", "id,nombre,especialidad");
        verificarYCrearArchivo("db/pacientes.csv", "id,nombre");
        verificarYCrearArchivo("db/citas.csv", "id,pacienteId,medicoId,fecha,motivo");
    }

    public static void verificarYCrearArchivo(String path, String headers) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.createNewFile()) {
                    if (headers != null && !headers.isEmpty()) {
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(headers + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Ha ocurrido un error al crear archivo: " + path);
            }
        }
    }

    public static void cargarMedicosDesdeArchivo() {
        medicos = Medico.obtenerTodos();
    }

    public static void cargarPacientesDesdeArchivo() {
        pacientes = Paciente.obtenerTodos();
    }

    public static void cargarCitasDesdeArchivo() {
        citas = Cita.obtenerTodos(pacientes, medicos);
    }

    private static boolean login() {
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String clave = scanner.nextLine();
        for (Admin admin : admins) {
            if (admin.autenticar(usuario, clave)) {
                return true;
            }
        }
        return false;
    }

    public static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n----- Menú -----");
            System.out.println("1. Dar de alta a médico");
            System.out.println("2. Dar de alta a paciente");
            System.out.println("3. Crear una cita");
            System.out.println("4. Mostrar todas las citas");
            System.out.println("5. Mostrar todos los médicos");
            System.out.println("6. Mostrar todos los pacientes");
            System.out.println("7. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    altaMedico();
                    break;
                case 2:
                    altaPaciente();
                    break;
                case 3:
                    crearCita();
                    break;
                case 4:
                    mostrarCitas();
                    break;
                case 5:
                    listarMedicos();
                    break;
                case 6:
                    listarPacientes();
                    break;
                case 7:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Error! opción inválida");
            }
        } while (opcion != 7);
        System.out.println("Hasta pronto!");
    }

    public static void altaMedico() {
        System.out.print("Nombre del medico: ");
        String nombre = scanner.nextLine();
        System.out.print("Especialidad del medico: ");
        String especialidad = scanner.nextLine();
        Medico medico = new Medico(nombre, especialidad);
        medico.guardar();
        medicos.add(medico);
        System.out.println("Médico " + medico + " ha sido añadido exitosamente!");
    }

    public static void altaPaciente() {
        System.out.print("Nombre del paciente: ");
        String nombre = scanner.nextLine();
        Paciente p = new Paciente(nombre);
        p.guardar();
        pacientes.add(p);
        System.out.println("Paciente " + p + " ha sido añadido exitosamente!");
    }

public static void crearCita() {
    if (pacientes.isEmpty() || medicos.isEmpty()) {
        System.out.println("Debe haber pacientes y médicos registrados para poder crear una cita.");
        return;
    }

    System.out.println("Selecciona un paciente:");
    for (int i = 0; i < pacientes.size(); i++) {
        System.out.println((i + 1) + ". " + pacientes.get(i));
    }
    int pIndex;
    try {
        pIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (pIndex < 0 || pIndex >= pacientes.size()) {
            System.out.println("Error! selección inválida.");
            return;
        }
    } catch (NumberFormatException e) {
        System.out.println("Error! entrada inválida.");
        return;
    }
    Paciente p = pacientes.get(pIndex);

    System.out.println("Selecciona un médico:");
    for (int i = 0; i < medicos.size(); i++) {
        System.out.println((i + 1) + ". " + medicos.get(i));
    }
    int mIndex;
    try {
        mIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (mIndex < 0 || mIndex >= medicos.size()) {
            System.out.println("Error! selección inválida.");
            return;
        }
    } catch (NumberFormatException e) {
        System.out.println("Error! entrada inválida.");
        return;
    }
    Medico m = medicos.get(mIndex);

    try {
        System.out.print("Favor de ingresar la fecha (yyyy-MM-dd): ");
        String fechaStr = scanner.nextLine();
        System.out.print("Ingrese hora (HH:mm): ");
        String horaStr = scanner.nextLine();

        System.out.print("Ingrese el motivo de la cita: ");
        String motivo = scanner.nextLine();

        String fechaCompleta = fechaStr + " " + horaStr;
        Date fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaCompleta);
        Cita c = new Cita(fechaHora, m, p, motivo);
        c.guardar();
        citas.add(c);
        System.out.println("Cita creada: " + c + " exitosamente");
    } catch (Exception e) {
        System.out.println("Error en estructura de fecha/hora!");
    }
}

    public static void mostrarCitas() {
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
        } else {
            for (Cita c : citas) {
                System.out.println(c);
            }
        }
    }

    public static void listarMedicos() {
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos cargados.");
        } else {
            System.out.println("Listado de médicos:");
            for (Medico m : medicos) {
                System.out.println(m);
            }
        }
    }

    public static void listarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes cargados.");
        } else {
            System.out.println("Listado de pacientes:");
            for (Paciente p : pacientes) {
                System.out.println(p);
            }
        }
    }

    static class Medico {
        String nombre;
        String especialidad;
        static int contador = 1;
        int id;

        public Medico(String nombre, String especialidad) {
            this.nombre = nombre;
            this.especialidad = especialidad;
            this.id = contador++;
        }

        public void guardar() {
            try (FileWriter fw = new FileWriter("db/medicos.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                String linea = id + "," + nombre + "," + especialidad;
                out.println(linea);
            } catch (IOException e) {
                System.out.println("Error al guardar médico en el archivo!");
            }
        }

        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre + ", Especialidad: " + especialidad;
        }

        public static List<Medico> obtenerTodos() {
            List<Medico> lista = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("db/medicos.csv"))) {
                String fila;
                br.readLine(); 
                while ((fila = br.readLine()) != null) {
                    String[] datos = fila.split(",");
                    if (datos.length >= 3) {
                        int id = Integer.parseInt(datos[0]);
                        String nombre = datos[1];
                        String especialidad = datos[2];
                        Medico m = new Medico(nombre, especialidad);
                        m.id = id;
                        lista.add(m);
                        if (id >= contador) {
                            contador = id + 1;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al cargar médicos desde archivo!");
            }
            return lista;
        }
    }

    static class Paciente {
        String nombre;
        static int contador = 1;
        int id;

        public Paciente(String nombre) {
            this.nombre = nombre;
            this.id = contador++;
        }

        public void guardar() {
            try (FileWriter fw = new FileWriter("db/pacientes.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                String linea = id + "," + nombre;
                out.println(linea);
            } catch (IOException e) {
                System.out.println("Error al guardar paciente en el archivo!");
            }
        }

        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre;
        }

        public static List<Paciente> obtenerTodos() {
            List<Paciente> lista = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("db/pacientes.csv"))) {
                String fila;
                br.readLine(); 
                while ((fila = br.readLine()) != null) {
                    String[] datos = fila.split(",");
                    if (datos.length >= 2) {
                        int id = Integer.parseInt(datos[0]);
                        String nombre = datos[1];
                        Paciente p = new Paciente(nombre);
                        p.id = id;
                        lista.add(p);
                        if (id >= contador) {
                            contador = id + 1;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al cargar pacientes desde archivo!");
            }
            return lista;
        }
    }

    static class Cita {
        Date fechaHora;
        Medico medico;
        Paciente paciente;
        String motivo;
        static int contador = 1;
        int id;

        public Cita(Date fechaHora, Medico medico, Paciente paciente, String motivo) {
        this.fechaHora = fechaHora;
        this.medico = medico;
        this.paciente = paciente;
        this.motivo = motivo;
}
         
        public void guardar() {
            try (FileWriter fw = new FileWriter("db/citas.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                String fechaStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fechaHora);
                String linea = id + "," + paciente.id + "," + medico.id + "," + fechaStr + "," + motivo;
                out.println(linea);
            } catch (IOException e) {
                System.out.println("Error al guardar cita en el archivo!");
            }
        }

        public String toString() {
            return "ID: " + id + ", Fecha: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fechaHora)
            + ", Médico: " + medico.nombre + " (" + medico.especialidad + ")"
            + ", Paciente: " + paciente.nombre
            + ", Motivo: " + motivo;
}

        public static List<Cita> obtenerTodos(List<Paciente> pacientes, List<Medico> medicos) {
            List<Cita> lista = new ArrayList<>();
            Map<Integer, Paciente> mapPacientes = new HashMap<>();
            for (Paciente p : pacientes) {
                mapPacientes.put(p.id, p);
            }
            Map<Integer, Medico> mapMedicos = new HashMap<>();
            for (Medico m : medicos) {
                mapMedicos.put(m.id, m);
            }
            try (BufferedReader br = new BufferedReader(new FileReader("db/citas.csv"))) {
                String fila;
                br.readLine(); 
                while ((fila = br.readLine()) != null) {
                    String[] datos = fila.split(",");
                    if (datos.length >= 5) {
                    int id = Integer.parseInt(datos[0]);
                    int pacienteId = Integer.parseInt(datos[1]);
                    int medicoId = Integer.parseInt(datos[2]);
                    String fechaStr = datos[3];
                    String motivo = datos[4]; 
                    Date fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaStr);
                    Paciente p = mapPacientes.get(pacienteId);
                    Medico m = mapMedicos.get(medicoId);
                    if (p != null && m != null) {
                    Cita c = new Cita(fechaHora, m, p, motivo);
                    c.id = id;
                   lista.add(c);
                   if (id >= contador) {
                  contador = id + 1;
                   }
                }
             }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar cita desde archivo!");
            }
            return lista;
        }
    }

    static class Admin {
        String usuario;
        String clave;

        public Admin(String usuario, String clave) {
            this.usuario = usuario;
            this.clave = clave;
        }

        public boolean autenticar(String u, String c) {
            return usuario.equals(u) && clave.equals(c);
        }
    }
}