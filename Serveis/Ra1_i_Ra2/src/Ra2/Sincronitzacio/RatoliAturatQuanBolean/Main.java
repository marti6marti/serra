package Ra2.Sincronitzacio.RatoliAturatQuanBolean;

/**
 * Main que demostra la diferència entre:
 * - Ratolins normals: mengen UNA vegada i acaben
 * - RatoliStoppable: menja en bucle fins que li diem que pari
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== INICI DE L'EXPERIMENT ===\n");

        // 1. CREEM DOS RATOLINS NORMALS
        // Aquests mengen UNA vegada i acaben sols
        Ratoli ratoli1 = new Ratoli("Ratolí Normal 1", 2, false);
        Ratoli ratoli2 = new Ratoli("Ratolí Normal 2", 3, false);

        // 2. CREEM UN RATOLÍ STOPPABLE
        // Aquest menjarà en bucle fins que li diguem que pari
        RatoliStoppable ratoli3 = new RatoliStoppable("Ratolí Infinit", 1, false);

        // 3. CREEM ELS THREADS
        Thread tratoli1 = new Thread(ratoli1, "Thread-Ratoli1");
        Thread tratoli2 = new Thread(ratoli2, "Thread-Ratoli2");
        Thread tratoli3 = new Thread(ratoli3, "Thread-RatoliStoppable");

        // 4. INICIEM TOTS ELS THREADS EN PARAL·LEL
        System.out.println("▶️  Iniciant tots els ratolins...\n");
        tratoli1.start();
        tratoli2.start();
        tratoli3.start();

        // 5. ESPEREM 6 SEGONS
        // Durant aquest temps:
        // - ratoli1 i ratoli2 acabaran sols (mengen 2 i 3 segons respectivament)
        // - ratoli3 continuarà menjant en bucle
        System.out.println("⏳ Esperant 6 segons...\n");
        Thread.sleep(6000);

        // 6. ATUREM NOMÉS EL RATOLÍ STOPPABLE
        System.out.println("🛑 Aturant el ratolí que encara menja...\n");
        ratoli3.stopRunning();

        // 7. ESPEREM QUE TOTS ELS THREADS ACABIN
        tratoli1.join();
        tratoli2.join();
        tratoli3.join();

        System.out.println("\n=== TOTS ELS RATOLINS HAN ACABAT ===");
    }
}