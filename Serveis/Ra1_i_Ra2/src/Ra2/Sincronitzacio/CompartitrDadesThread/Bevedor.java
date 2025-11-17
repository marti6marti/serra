package Ra2.Sincronitzacio.CompartitrDadesThread;

import java.util.Random;

/**
 * Classe Bevedor modificada que pot demanar noves ampolles al cambrer.
 *
 * DIFERÈNCIA AMB EX 8:
 * - Ara té una referència al cambrer
 * - Quan l'ampolla s'acaba, demana una nova al cambrer
 * - Continua bevent amb la nova ampolla
 */
public class Bevedor implements Runnable {

    private Got got;
    private Ampolla ampolla;
    private final Cambrer cambrer; // ⬅️ NOU: referència al cambrer

    // Constructor AMB cambrer (3 paràmetres)
    public Bevedor(Got got, Ampolla ampolla, Cambrer cambrer) {
        this.got = got;
        this.ampolla = ampolla;
        this.cambrer = cambrer; // ⬅️ Guardem la referència
    }

    // Getters i Setters
    public Got getGot() {
        return got;
    }

    public void setGot(Got got) {
        this.got = got;
    }

    public Ampolla getAmpolla() {
        return ampolla;
    }

    public void setAmpolla(Ampolla ampolla) {
        this.ampolla = ampolla;
    }

    /**
     * Mètode NOU: Demana més beguda al cambrer.
     *
     * COORDINACIÓ:
     * - El bevedor detecta que l'ampolla està buida
     * - Demana una nova ampolla al cambrer
     * - Actualitza la seva referència i la del got
     *
     * @param capacitat Capacitat de la nova ampolla
     */
    private void demanarMesBeguda(int capacitat) {
        Thread t = Thread.currentThread();

        if (ampolla.getQuantitatClRestants() <= 0) {
            System.out.println("\n🔔 [" + t.getName() + "] L'ampolla està buida! Demanant nova ampolla...");

            // Demanem nova ampolla al cambrer (mètode synchronized)
            ampolla = cambrer.donarNovaAmpolla(capacitat);

            // Actualitzem la referència del got també
            got.setAmpolla(ampolla);

            System.out.println("   [" + t.getName() + "] Nova ampolla rebuda!\n");
        }
    }

    /**
     * Mètode principal MODIFICAT per usar el cambrer.
     */
    public void beure() {
        Thread t = Thread.currentThread();
        Random random = new Random();

        System.out.println("🙋 [" + t.getName() + "] Ha arribat i comença a beure!");

        // Primer omplim el got
        if (got.getContingutActual() == 0) {
            got.omplirGotAmbAmpolla();
        }

        // Bucle principal: mentre hi hagi contingut al got
        while (got.getContingutActual() > 0) {

            // BEU UN GLOP de 2 cL
            got.reduirContingut(2);
            System.out.println("😋 [" + t.getName() + "] *glop* Contingut restant al got: " +
                    got.getContingutActual() + " cL");

            // DORM TEMPS ALEATORI (1-6 segons)
            int segons = random.nextInt(6) + 1;
            try {
                Thread.sleep(segons * 1000L);
            } catch (InterruptedException e) {
                System.out.println("⚠️  [" + t.getName() + "] ha estat interromput mentre bevia.");
                Thread.currentThread().interrupt();
                return;
            }

            // SI EL GOT S'HA ACABAT, intenta omplir-lo
            if (got.getContingutActual() <= 0) {
                System.out.println("🫗 [" + t.getName() + "] El got està buit!");

                // ⬅️ NOU: Si l'ampolla està buida, demana nova
                demanarMesBeguda(50);

                // Comprova si l'ampolla té líquid (pot ser la nova o l'antiga)
                if (ampolla.getQuantitatClRestants() > 0) {
                    System.out.println("🔄 [" + t.getName() + "] Tornant a omplir el got...");
                    got.omplirGotAmbAmpolla();
                } else {
                    // L'ampolla està buida, deixa de beure
                    System.out.println("🛑 [" + t.getName() + "] No hi ha més beguda disponible!");
                    break;
                }
            }
        }

        System.out.println("👋 [" + t.getName() + "] Ha acabat de beure i marxa!");
    }

    @Override
    public void run() {
        beure();
    }
}