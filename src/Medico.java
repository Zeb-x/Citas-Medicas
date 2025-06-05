import java.util.*;

public class Medico {
    private static int idCounter = 1;
    private int id;
    private String nombre;

    public Medico(String nombre) {
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

    public static Medico fromCSV(String linea) {
        String[] datos = linea.split(",");
        if (datos.length == 2) {
            try {
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                Medico m = new Medico(nombre);
                m.id = id;
                if (id >= idCounter) {
                    idCounter = id + 1;
                }
                return m;
            } catch (NumberFormatException e) {
                System.out.println("Ha ocurrido un error al crear la línea: " + linea);
            }
        }
        return null;
    }

    public void guardar() {
        ArchivoManager.agregarLinea("medicos.csv", this.toCSV());
    }

    public static List<Medico> obtenerTodos() {
        List<Medico> lista = new ArrayList<>();
        ArchivoManager.crearArchivoSiNoExiste("medicos.csv");
        List<String> lineas = ArchivoManager.leerArchivo("medicos.csv");
        for (String linea : lineas) {
            Medico m = fromCSV(linea);
            if (m != null) {
                lista.add(m);
            }
        }
        return lista;
    }
}