package Ra2.Sincronitzacio.RatoliAturatQuanBolean;

public class Ratoli implements Runnable {

    private String nom;
    private int tempsQtrigaEnMenjar; // en segons
    private boolean haDitMissatge;

    // Constructors
    public Ratoli(String nom, int tempsQtrigaEnMenjar, boolean haDitMissatge) {
        this.nom = nom;
        this.tempsQtrigaEnMenjar = tempsQtrigaEnMenjar;
        this.haDitMissatge = haDitMissatge;
    }

    public Ratoli() {
        this.nom = "Ratolí sense nom";
        this.tempsQtrigaEnMenjar = 3;
        this.haDitMissatge = false;
    }

    // Getters i Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTempsQtrigaEnMenjar() {
        return tempsQtrigaEnMenjar;
    }

    public void setTempsQtrigaEnMenjar(int tempsQtrigaEnMenjar) {
        this.tempsQtrigaEnMenjar = tempsQtrigaEnMenjar;
    }

    public boolean isHaDitMissatge() {
        return haDitMissatge;
    }

    public void setHaDitMissatge(boolean haDitMissatge) {
        this.haDitMissatge = haDitMissatge;
    }

    /**
     * Mètode que simula que el ratolí menja UNA VEGADA.
     */
    public void menja() {
        Thread t = Thread.currentThread();

        System.out.println("El ratolí " + nom + " ha començat a menjar");

        try {
            // Simula el temps que triga a menjar
            Thread.sleep(tempsQtrigaEnMenjar * 1000L);
        } catch (InterruptedException e) {
            System.out.println("El ratolí " + nom + " ha estat interromput mentre menjava");
            Thread.currentThread().interrupt();
        }

        System.out.println("El ratolí " + nom + " està menjant");
        System.out.println("El ratolí " + nom + " ha acabat de menjar");
    }

    /**
     * Mètode alternatiu per menjar (més curt, per usar en bucles).
     */
    public void menja2() {
        Thread t = Thread.currentThread();

        System.out.println("[" + t.getName() + "] " + nom + " està menjant...");

        try {
            Thread.sleep(tempsQtrigaEnMenjar * 1000L);
        } catch (InterruptedException e) {
            System.out.println("[" + t.getName() + "] " + nom + " interromput!");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        // Un ratolí normal només menja UNA vegada
        this.menja();
    }
}