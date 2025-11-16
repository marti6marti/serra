package Ra2.Intro.CrearThread;

public class Ratoli extends Thread {
    private String nom;

    // Constructor
    public Ratoli(String nom) {
        this.nom = nom;
    }

    // Getters i setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Mètode que fa l'acció de menjar
    public void menja() {
        System.out.println("El ratolí " + nom + " ha començat a menjar");
        System.out.println("El ratolí " + nom + " està menjant");
        System.out.println("El ratolí " + nom + " ha acabat de menjar");
    }

    // Mètode run() que s'executarà quan cridem start()
    @Override
    public void run() {
        this.menja();
    }
}