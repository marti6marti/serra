package Ra2.Sincronitzacio.CompartitrDadesThread;

/**
 * Main que demostra dos bevedors amb un cambrer que els proporciona ampolles.
 *
 * OBJECTIU:
 * - Veure com un tercer fil (cambrer) gestiona recursos compartits
 * - Coordinar threads: bevedors depenen del cambrer
 * - Observar col·laboració entre threads, no només competència
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  EXERCICI 9: BEVEDORS AMB CAMBRER             ║");
        System.out.println("║  (Coordinació entre threads)                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // 1. CREEM L'AMPOLLA INICIAL (petita per forçar que demanin més)
        Ampolla ampolla = new Ampolla(30); // Només 30 cL per començar
        System.out.println("🍾 Ampolla inicial: " + ampolla.getQuantitatClRestants() + " cL\n");

        // 2. CREEM ELS GOTS
        Got gotMarcos = new Got(15);
        Got gotJulia = new Got(10);

        // 3. ASSIGNEM L'AMPOLLA ALS GOTS
        gotMarcos.setAmpolla(ampolla);
        gotJulia.setAmpolla(ampolla);

        // 4. CREEM EL CAMBRER ⬅️ NOU!
        Cambrer cambrer = new Cambrer();

        // 5. CREEM ELS BEVEDORS AMB CAMBRER ⬅️ Ara passem el cambrer
        Bevedor bevedor1 = new Bevedor(gotMarcos, ampolla, cambrer);
        Bevedor bevedor2 = new Bevedor(gotJulia, ampolla, cambrer);

        // 6. CREEM ELS THREADS
        Thread tBevedor1 = new Thread(bevedor1, "Marcos");
        Thread tBevedor2 = new Thread(bevedor2, "Júlia");

        // 7. INICIEM
        System.out.println("▶️  Iniciant els bevedors...\n");
        tBevedor1.start();
        tBevedor2.start();

        // 8. ESPEREM
        tBevedor1.join();
        tBevedor2.join();

        // 9. RESULTAT
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║  RESULTAT FINAL                               ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println("🍾 Ampolla final: " + ampolla.getQuantitatClRestants() + " cL");

        System.out.println("\n💡 COORDINACIÓ OBSERVADA:");
        System.out.println("   - Els bevedors han demanat noves ampolles al cambrer");
        System.out.println("   - El cambrer ha proporcionat ampolles de manera sincronitzada");
        System.out.println("   - Els threads col·laboren, no només competeixen");
    }
}
