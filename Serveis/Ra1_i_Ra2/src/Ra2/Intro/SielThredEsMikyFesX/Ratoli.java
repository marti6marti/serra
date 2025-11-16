package Ra2.Intro.SielThredEsMikyFesX;

public class Ratoli implements Runnable {
    private String nom;
    private int tempsQtrigaEnMenjar;
    private boolean haDitMissatge = false;  // ← Nou atribut per controlar el missatge

    // Constructor
    public Ratoli(String nom, int tempsQtrigaEnMenjar) {
        this.nom = nom;
        this.tempsQtrigaEnMenjar = tempsQtrigaEnMenjar;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public int getTempsQtrigaEnMenjar() {
        return tempsQtrigaEnMenjar;
    }

    // Mètode menja amb la comprovació del nom del thread
    public void menja() {
        Thread threadActual = Thread.currentThread();  // ← Obtenim el thread actual

        try {
            // Comprovem si el nom del thread és "Mickey" i encara no ha dit el missatge
            if (threadActual.getName().equals("Mickey") && !haDitMissatge) {
                System.out.println("💕 Mickey loves Minnie!");
                haDitMissatge = true;  // Marquem que ja s'ha dit
            }

            System.out.println("🍽️  El ratolí " + threadActual.getName() + " ha començat a menjar.");
            Thread.sleep(tempsQtrigaEnMenjar * 1000L);
            System.out.println("✅ El ratolí " + threadActual.getName() + " ha acabat de menjar.");

        } catch (InterruptedException e) {
            System.out.println("❌ El ratolí " + threadActual.getName() + " ha estat interromput mentre menjava.");
        }
    }

    @Override
    public void run() {
        this.menja();
    }
}
