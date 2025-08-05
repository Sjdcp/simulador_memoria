public class Proceso {
    private static int contadorPID = 1;
    private int pid;
    private String nombre;
    private int memoriaRequerida; // en MB
    private int duracion; // en segundos
    private Estado estado;

    public enum Estado {
        EN_ESPERA,
        EN_EJECUCION,
        FINALIZADO
    }

    public Proceso(String nombre, int memoriaRequerida, int duracion) {
        this.pid = contadorPID++;
        this.nombre = (nombre == null || nombre.isEmpty()) ? "Proceso-" + pid : nombre;
        this.memoriaRequerida = memoriaRequerida;
        this.duracion = duracion;
        this.estado = Estado.EN_ESPERA;
    }

    public int getPid() { return pid; }
    public String getNombre() { return nombre; }
    public int getMemoriaRequerida() { return memoriaRequerida; }
    public int getDuracion() { return duracion; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombre + " (PID: " + pid + ", " + memoriaRequerida + "MB, " + estado + ")";
    }
}