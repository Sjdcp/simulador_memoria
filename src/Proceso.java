public class Proceso {
    private static int contadorId = 1;
    private final int pid;//Identificador único del proceso
    private final String nombre;//Nombre del proceso
    private final int memoria;//en MB
    private final int duracion;//en segundos

    public enum Estado {
        EN_ESPERA,
        EN_EJECUCION,
        FINALIZADO
    }
// en proceso
    public Proceso(String nombre, int memoriaRequerida, int duracion) {
        this.pid = contadorPID++;
        this.nombre = (nombre == null || nombre.isEmpty()) ? "Proceso-" + pid : nombre;
        this.memoria = memoria;
        this.duracion = duracion;
    }

    
    public int getMemoria() {
        return memoria;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %dMB - %ds", pid, nombre, memoria, duracion);
    }
}
