import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
    public static void verificarYCrearArchivo(String path, String headers) {
        File file = new File(path);
        if (!file.exists()) {
            try {

                File parentDir = new File(file.getParent());
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
               
                if (file.createNewFile()) {
                   
                    if (headers != null && !headers.isEmpty()) {
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(headers + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al crear archivo: " + path);
            }
        }
    }

    public static void verificarArchivos() {
        verificarYCrearArchivo("db/medicos.csv", "id,nombre");
        verificarYCrearArchivo("db/pacientes.csv", "id,nombre");
        verificarYCrearArchivo("db/citas.csv", "id,pacienteId,medicoId,fecha,motivo");
    }
}