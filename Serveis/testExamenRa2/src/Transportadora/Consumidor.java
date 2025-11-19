package Transportadora;

public class Consumidor implements Runnable {
    private Cinta cinta;
    private int id;
    private int numeroPaquetes;

    public Consumidor(Cinta cinta, int id, int numeroPaquetes) {
        this.cinta = cinta;
        this.id = id;
        this.numeroPaquetes = numeroPaquetes;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < numeroPaquetes; i++) {
                cinta.retirarPaquete(id);
                Thread.sleep((int)(Math.random() * 500));
            }
            System.out.println("Consumidor " + id + " ha terminado");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
