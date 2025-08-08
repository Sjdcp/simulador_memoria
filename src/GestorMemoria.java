import java.util.ArrayList;
import java.util.List;

public class GestorMemoria {
    private final int memoriaTotal = 1024;
    private int memoriaDisponible = memoriaTotal;

    private final List<Proceso> enEjecucion = new ArrayList<>();
    private final List<Proceso> enEspera = new ArrayList<>();
    private final List<Proceso> finalizados = new ArrayList<>();

    public int getMemoriaTotal() {
        return memoriaTotal;
    }

    public int getMemoriaDisponible() {
        return memoriaDisponible;
    }

    public List<Proceso> getEnEjecucion() {
        return enEjecucion;
    }

    public List<Proceso> getEnEspera() {
        return enEspera;
    }

    public List<Proceso> getFinalizados() {
        return finalizados;
    }

    public void agregarProceso(Proceso p, Runnable actualizarUI) {
        if (p.getMemoria() <= memoriaDisponible) {
            ejecutarProceso(p, actualizarUI);
        } else {
            enEspera.add(p);
            actualizarUI.run();
        }
    }

    private void ejecutarProceso(Proceso p, Runnable actualizarUI) {
        enEjecucion.add(p);
        memoriaDisponible -= p.getMemoria();
        actualizarUI.run();

        new Thread(() -> {
            try {
                Thread.sleep(p.getDuracion() * 1000L);
            } catch (InterruptedException e) {
                System.err.println("Error en proceso: " + e.getMessage());
            }
            finalizarProceso(p, actualizarUI);
        }).start();
    }

    private void finalizarProceso(Proceso p, Runnable actualizarUI) {
        enEjecucion.remove(p);
        memoriaDisponible += p.getMemoria();
        finalizados.add(p);

        if (!enEspera.isEmpty()) {
            List<Proceso> pendientes = new ArrayList<>(enEspera);
            for (Proceso proc : pendientes) {
                if (proc.getMemoria() <= memoriaDisponible) {
                    enEspera.remove(proc);
                    ejecutarProceso(proc, actualizarUI);
                }
            }
        }
        actualizarUI.run();
    }
}
