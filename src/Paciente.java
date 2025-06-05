import java.util.*;

public class Paciente {
    private static int idCounter = 1;
    private int id;
    private String nombre;

    public Paciente(String nombre) {
        this.id = idCounter++;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", '" + nombre + "'}";
    }

    public String toCSV() {
        return id + "," + nombre;
    }

    public static Paciente fromCSV(String linea) {
        String[] datos = linea.split(",");
        if (datos.length >= 2) {
            try {
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                Paciente p = new Paciente(nombre);
                p.id = id;
                if (id >= idCounter) {
                    idCounter = id + 1;
                }
                return p;
            } catch (NumberFormatException e) {
                System.out.println("Ha ocurrido un error al crear línea: " + linea);
            }
        }
        return null;
    }

    public void guardar() {
        ArchivoManager.agregarLinea("pacientes.csv", this.toCSV());
    }

    public static List<Paciente> obtenerTodos() {
        List<Paciente> lista = new ArrayList<>();
        ArchivoManager.crearArchivoSiNoExiste("pacientes.csv");
        List<String> lineas = ArchivoManager.leerArchivo("pacientes.csv");
        for (String linea : lineas) {
            Paciente p = fromCSV(linea);
            if (p != null) {
                lista.add(p);
            }
        }
        return lista;
    }
}