import java.io.*;
import java.util.*;

public class ArchivoManager {
    public static void crearCarpetaSiNoExiste() {
        File carpeta = new File(System.getProperty("user.dir") + File.separator + "db");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public static File getArchivo(String nombreArchivo) {
        return new File(System.getProperty("user.dir") + File.separator + "db", nombreArchivo);
    }

    public static void crearArchivoSiNoExiste(String nombreArchivo) {
        crearCarpetaSiNoExiste();
        File archivo = getArchivo(nombreArchivo);
        if (!archivo.exists()) {
            try {
                if (archivo.createNewFile()) {
  
                }
            } catch (IOException e) {
                System.out.println("Error al crear el archivo " + nombreArchivo);
            }
        }
    }

    public static List<String> leerArchivo(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        File archivo = getArchivo(nombreArchivo);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas.add(linea);
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo " + nombreArchivo);
            }
        }
        return lineas;
    }

    public static void agregarLinea(String filename, String linea) {
        try (FileWriter fw = new FileWriter(new File(System.getProperty("user.dir") + File.separator + "db", filename), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}