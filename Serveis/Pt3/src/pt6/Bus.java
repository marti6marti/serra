package pt6;

public class Bus {
    private final int capacitat;
    private int ocupacio = 0;

    public Bus(int capacitat) {
        this.capacitat = capacitat;
    }

    public synchronized void pujar(String nom) {
        while (ocupacio == capacitat) {
            try {
                System.out.println(nom + " espera: el bus està ple.");
                wait();
            } catch (InterruptedException e) {
                System.out.println(nom + " s'ha cansat d'esperar i marxa.");
                return;
            }
        }
        ocupacio++;
        System.out.println(nom + " ha pujat. Ocupació: " + ocupacio + "/" + capacitat);
    }


    public synchronized void baixar(String nom) {
        ocupacio--;
        System.out.println(nom + " ha baixat. Ocupació: " + ocupacio + "/" + capacitat);
        notifyAll();
    }
}
