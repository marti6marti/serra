package pt6;

public class Passatger extends Thread {
    private final Bus bus;
    private final String nom;

    public Passatger(String nom, Bus bus) {
        this.nom = nom;
        this.bus = bus;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                bus.pujar(nom);
                Thread.sleep((int) (Math.random() * 3000));
                bus.baixar(nom);
                Thread.sleep((int) (Math.random() * 2000));
            }
        } catch (InterruptedException e) {
            System.out.println(nom + " ha estat interromput.");
        }
    }
}

