package Ra2.Sincronitzacio.RatoliQueEsperaJoin;

import java.util.ArrayList;


public class RatoliPareJoin extends Ratoli implements Runnable {

    /**
     * Llista que conté els threads dels ratolins fills.
     * El pare cridarà join() sobre cada un d'aquests threads.
     */
    private ArrayList<Thread> llistaRatolinsJoin = new ArrayList<>();

    // Constructors
    public RatoliPareJoin(String nom, int tempsQtrigaEnMenjar, boolean haDitMissatge) {
        super(nom, tempsQtrigaEnMenjar, haDitMissatge);
    }

    public RatoliPareJoin() {
        super();
    }

    // Getters i Setters
    public ArrayList<Thread> getLlistaRatolinsJoin() {
        return llistaRatolinsJoin;
    }

    public void setLlistaRatolinsJoin(ArrayList<Thread> llistaRatolinsJoin) {
        this.llistaRatolinsJoin = llistaRatolinsJoin;
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();

        System.out.println("\n👨 [" + t.getName() + "] " + getNom() + " està esperant que els seus fills acabin de menjar...");
        System.out.println("   (Utilitzant join() per esperar)\n");

        // Recorrem cada thread fill
        for (Thread ratolinsJoinFills : llistaRatolinsJoin) {
            System.out.println("   ⏳ [" + t.getName() + "] Esperant que " + ratolinsJoinFills.getName() + " acabi...");

            try {
                ratolinsJoinFills.join();
                // Quan arribem aquí, el fill JA ha acabat
                System.out.println("   ✅ [" + t.getName() + "] " + ratolinsJoinFills.getName() + " ha acabat!");

            } catch (InterruptedException e) {
                System.out.println("   ⚠️ [" + t.getName() + "] ha estat interromput mentre esperava " + ratolinsJoinFills.getName());
                // Restablim l'estat d'interrupció
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Tots els fills han acabat (o hem estat interromputs)
        if (!Thread.currentThread().isInterrupted()) {
            System.out.println("\n🎉 [" + t.getName() + "] " + getNom() + " Tots els fills han acabat! Ara començo a menjar jo!\n");
            // El pare menja
            super.menja2();
            System.out.println("\n👍 [" + t.getName() + "] " + getNom() + " ha acabat de menjar!");
        } else {
            System.out.println("\n⚠️ [" + t.getName() + "] " + getNom() + " ha estat interromput i no menjarà.");
        }
    }
}