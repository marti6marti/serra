package Ra2.Sincronitzacio.CompartitrDadesThread;

/**
 * Classe Got que conté líquid i pot omplir-se des d'una ampolla.
 *
 * CONCEPTE:
 * - Cada bevedor té el seu propi got
 * - Però tots els gots s'omplen de la MATEIXA ampolla (recurs compartit)
 */
public class Got {

    private int capacitatMaxima;
    private int contingutActual;
    private Ampolla ampolla; // Referència a l'ampolla compartida

    // Constructor
    public Got(int capacitatMaxima) {
        this.capacitatMaxima = capacitatMaxima;
        this.contingutActual = 0;
    }

    // Getters i Setters
    public int getCapacitatMaxima() {
        return capacitatMaxima;
    }

    public void setCapacitatMaxima(int capacitatMaxima) {
        this.capacitatMaxima = capacitatMaxima;
    }

    public Ampolla getAmpolla() {
        return ampolla;
    }

    public void setAmpolla(Ampolla ampolla) {
        this.ampolla = ampolla;
    }

    public int getContingutActual() {
        return contingutActual;
    }

    public void setContingutActual(int contingutActual) {
        this.contingutActual = contingutActual;
    }

    /**
     * Redueix el contingut del got (quan el bevedor beu).
     */
    public void reduirContingut(int cl) {
        contingutActual = Math.max(0, contingutActual - cl);
    }

    /**
     * Omple el got des de l'ampolla compartida.
     *
     * PUNT DE CONCURRÈNCIA:
     * - Dos bevedors poden cridar aquest mètode alhora
     * - Els dos accediran a ampolla.servirQuantitat()
     * - Sense sincronització, hi haurà problemes!
     */
    public void omplirGotAmbAmpolla() {
        Thread t = Thread.currentThread();

        // Validacions
        if (ampolla == null) {
            System.out.println("❌ [" + t.getName() + "] No hi ha cap ampolla assignada al got!");
            return;
        }

        if (ampolla.getQuantitatClRestants() <= 0) {
            System.out.println("❌ [" + t.getName() + "] L'ampolla està buida! No es pot emplenar el got.");
            return;
        }

        // Calcular quanta quantitat servir
        int quantitatAServir = Math.min(capacitatMaxima, ampolla.getQuantitatClRestants());

        System.out.println("🥤 [" + t.getName() + "] Omplint el got amb " + quantitatAServir + " cL...");

        // CRIDA AL RECURS COMPARTIT (ampolla)
        ampolla.servirQuantitat(quantitatAServir);

        // Actualitzem el contingut del got
        contingutActual = quantitatAServir;

        System.out.println("✅ [" + t.getName() + "] Got emplenat! Contingut: " + contingutActual + " cL");
    }
}
