import java.util.*;
import java.text.SimpleDateFormat;

public class Cita {
    private static int idCounter = 1;
    private int id;
    private Date fechaHora;
    private Medico medico;
    private Paciente paciente;
    private String motivo; 

public Cita(Date fechaHora, Medico medico, Paciente paciente, String motivo) {
    this.id = idCounter++;
    this.fechaHora = fechaHora;
    this.medico = medico;
    this.paciente = paciente;
    this.motivo = motivo;
}

    public int getId() {
        return id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    @Override
    public String toString() {
        String fechaStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fechaHora);
        return ": id: " + id + ", Fecha/Hora=" + fechaStr +
               ", motivo=" + motivo +
               ", medico: " + medico.getNombre() +
               ", paciente: " + paciente.getNombre();
    }

    public String toCSV() {
        String fechaStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fechaHora);
        return id + "," + paciente.getId() + "," + medico.getId() + "," + fechaStr + "," + motivo;
    }

    public void guardar() {
        ArchivoManager.agregarLinea("citas.csv", this.toCSV());
    }

    public static List<Cita> obtenerTodos(List<Paciente> pacientes, List<Medico> medicos) {
        List<Cita> lista = new ArrayList<>();
        ArchivoManager.crearArchivoSiNoExiste("citas.csv");
        List<String> lineas = ArchivoManager.leerArchivo("citas.csv");
        for (String linea : lineas) {
            String[] datos = linea.split(",");
            if (datos.length >= 5) { 
                try {
                    int id = Integer.parseInt(datos[0]);
                    int pacienteId = Integer.parseInt(datos[1]);
                    int medicoId = Integer.parseInt(datos[2]);
                    String fechaStr = datos[3];
                    String motivo = datos[4]; 

                    Date fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaStr);

                    Paciente p = null;
                    for (Paciente paciente : pacientes) {
                        if (paciente.getId() == pacienteId) {
                            p = paciente;
                            break;
                        }
                    }

                    Medico m = null;
                    for (Medico medico : medicos) {
                        if (medico.getId() == medicoId) {
                            m = medico;
                            break;
                        }
                    }

                    if (p != null && m != null) {
                        Cita c = new Cita(fechaHora, m, p, motivo);
                        c.id = id;
                        if (id >= idCounter) {
                            idCounter = id + 1;
                        }
                        lista.add(c);
                    }
                } catch (Exception e) {
                    System.out.println("Ha ocurrido un error al crear cita: " + linea);
                }
            }
        }
        return lista;
    }
}