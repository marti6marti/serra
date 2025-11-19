package Transportadora;

public class Productor implements Runnable {
    private Cinta cinta;
    private int id;
    private int numeroPaquetes;

    public Productor(Cinta cinta, int id, int numeroPaquetes) {
        this.cinta = cinta;
        this.id = id;
        this.numeroPaquetes = numeroPaquetes;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= numeroPaquetes; i++) {
                cinta.ponerPaquete(id, i);
                Thread.sleep((int)(Math.random() * 500)); // Duerme un poco (simula trabajo)
            }
            System.out.println("Productor " + id + " ha terminado");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
