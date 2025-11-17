package Ra2.Sincronitzacio.CompartitrDadesThread;

/**
 * Classe Cambrer que proporciona noves ampolles als bevedors.
 *
 * CONCEPTE CLAU:
 * - Actua com a "gestor de recursos"
 * - Proporciona noves ampolles quan la compartida s'acaba
 * - Els mètodes són synchronized per evitar problemes de concurrència
 *
 * COORDINACIÓ ENTRE THREADS:
 * - Els bevedors (threads) demanen ampolles al cambrer
 * - El cambrer (mètode synchronized) proporciona l'ampolla
 * - Això crea una relació de col·laboració, no només competència
 */
public class Cambrer {

    /**
     * Proporciona una nova ampolla amb la capacitat especificada.
     *
     * synchronized: Només un thread pot demanar ampolla alhora.
     * Això evita que dos bevedors demanin simultàniament i
     * es creïn dues ampolles quan només n'hauria d'haver una.
     *
     * @param capacitat Capacitat de la nova ampolla en cL
     * @return Nova ampolla creada
     */
    public synchronized Ampolla donarNovaAmpolla(int capacitat) {
        Thread t = Thread.currentThread();

        System.out.println("\n🤵 [CAMBRER] " + t.getName() + " ha demanat una nova ampolla.");
        System.out.println("   Creant nova ampolla amb " + capacitat + " cL...");

        // Simulem que triga una mica a preparar l'ampolla
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Ampolla novaAmpolla = new Ampolla(capacitat);
        System.out.println("   ✅ Nova ampolla preparada amb " + capacitat + " cL");

        return novaAmpolla;
    }
}