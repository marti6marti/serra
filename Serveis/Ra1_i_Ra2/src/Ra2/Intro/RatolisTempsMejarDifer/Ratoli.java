package Ra2.Intro.RatolisTempsMejarDifer;

public class Ratoli implements Runnable {
    private String nom;
    private int tempsQtrigaEnMenjar;  // En segons

    // Constructor buit
    public Ratoli() {
    }

    // Constructor amb paràmetres
    public Ratoli(String nom, int tempsQtrigaEnMenjar) {
        this.nom = nom;
        this.tempsQtrigaEnMenjar = tempsQtrigaEnMenjar;
    }

    // Getters i setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTempsQtrigaEnMenjar() {
        return tempsQtrigaEnMenjar;
    }

    public void setTempsMenjar(int temps) {
        this.tempsQtrigaEnMenjar = temps;
    }

    // Mètode per menjar amb sleep
    public void menja() {
        try {
            System.out.println("🍽️  " + nom + " ha començat a menjar.");
            Thread.sleep(tempsQtrigaEnMenjar * 1000L);  // Convertim segons a mil·lisegons
            System.out.println("✅ " + nom + " ha acabat de menjar! (va trigar " + tempsQtrigaEnMenjar + " segons)");
        } catch (InterruptedException e) {
            System.out.println("❌ " + nom + " ha estat interromput mentre menjava.");
        }
    }

    @Override
    public void run() {
        this.menja();
    }
}