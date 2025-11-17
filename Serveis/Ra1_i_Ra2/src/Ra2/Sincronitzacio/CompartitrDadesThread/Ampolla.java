package Ra2.Sincronitzacio.CompartitrDadesThread;

public class Ampolla {

    private int quantitatClRestants;

    // Constructor
    public Ampolla(int quantitatClRestants) {
        this.quantitatClRestants = quantitatClRestants;
    }

    // Getters i Setters
    public int getQuantitatClRestants() {
        return quantitatClRestants;
    }

    public void setQuantitatClRestants(int quantitatClRestants) {
        this.quantitatClRestants = quantitatClRestants;
    }

    public void servirQuantitat(int centilitres) {
        Thread t = Thread.currentThread();

        // Comprovació si està buida
        if (quantitatClRestants <= 0) {
            System.out.println("❌ [" + t.getName() + "] L'ampolla està buida! No es pot servir res.");
            return;
        }

        // Comprovació si hi ha prou líquid
        if (quantitatClRestants < centilitres) {
            System.out.println("⚠️  [" + t.getName() + "] No hi ha prou líquid a l'ampolla.");
            System.out.println("   Només es serviran " + quantitatClRestants + " cL (demanaven " + centilitres + " cL)");
            centilitres = quantitatClRestants;
        }

        // PUNT CRÍTIC: aquí pot haver-hi problemes de concurrència
        System.out.println("🍾 [" + t.getName() + "] Servint " + centilitres + " cL. Abans hi havia: " + quantitatClRestants + " cL");

        // Aquesta operació NO és atòmica sense synchronized
        quantitatClRestants = quantitatClRestants - centilitres;

        System.out.println("   [" + t.getName() + "] Ara queden: " + quantitatClRestants + " cL a l'ampolla");
    }
}
