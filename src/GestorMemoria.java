import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestorMemoria {
    private final int memoriaTotal = 1024; // 1 GB
    private int memoriaDisponible;
    private final List<Proceso> enEjecucion = new ArrayList<>();
    private final List<Proceso> enEspera = new ArrayList<>();

    public GestorMemoria() {
        this.memoriaDisponible = memoriaTotal;
    }

    public List<Proceso> getEnEjecucion() { return enEjecucion; }
    public List<Proceso> getEnEspera() { return enEspera; }
    public int getMemoriaDisponible() { return memoriaDisponible; }
    public int getMemoriaTotal() { return memoriaTotal; }

    public void agregarProceso(Proceso proceso, Runnable actualizarUI) {
        if (proceso.getMemoriaRequerida() <= memoriaDisponible) {
            ejecutarProceso(proceso, actualizarUI);
        } else {
            enEspera.add(proceso);
        }
        actualizarUI.run();
    }

    private void ejecutarProceso(Proceso proceso, Runnable actualizarUI) {
        memoriaDisponible -= proceso.getMemoriaRequerida();
        proceso.setEstado(Proceso.Estado.EN_EJECUCION);
        enEjecucion.add(proceso);

        new Thread(() -> {
            try {
                Thread.sleep(proceso.getDuracion() * 1000L);
                finalizarProceso(proceso, actualizarUI);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void finalizarProceso(Proceso proceso, Runnable actualizarUI) {
        proceso.setEstado(Proceso.Estado.FINALIZADO);
        memoriaDisponible += proceso.getMemoriaRequerida();
        enEjecucion.remove(proceso);

        revisarCola(actualizarUI);
        actualizarUI.run();
    }

    private void revisarCola(Runnable actualizarUI) { 
        Iterator<Proceso> iterator = enEspera.iterator();
        while (iterator.hasNext()) {
            Proceso proceso = iterator.next();
            if (proceso.getMemoriaRequerida() <= memoriaDisponible) {
                iterator.remove();
                ejecutarProceso(proceso, actualizarUI);
            }
        }
    }
}