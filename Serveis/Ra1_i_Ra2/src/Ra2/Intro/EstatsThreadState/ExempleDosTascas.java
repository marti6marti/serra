package Ra2.Intro.EstatsThreadState;

public class ExempleDosTascas {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("   OBSERVANT ELS ESTATS DE DOS FILS");
        System.out.println("═══════════════════════════════════════════════\n");

        // Creem dues tasques diferents
        Runnable tascaMickey = () -> {
            try {
                System.out.println("   🐭 [MICKEY] Començo a menjar...");
                Thread.sleep(3000);  // Dorm 3 segons
                System.out.println("   🐭 [MICKEY] He acabat de menjar!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable tascaMinnie = () -> {
            try {
                System.out.println("   🎀 [MINNIE] Començo a menjar...");
                Thread.sleep(2000);  // Dorm 2 segons
                System.out.println("   🎀 [MINNIE] He acabat de menjar!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Creem els dos Threads
        Thread mickey = new Thread(tascaMickey, "Mickey");
        Thread minnie = new Thread(tascaMinnie, "Minnie");


        // ════════════════════════════════════════════════════════════
        // 1️⃣ ESTAT: NEW (acabats de crear)
        // ════════════════════════════════════════════════════════════
        System.out.println("1️⃣ ESTAT INICIAL (després de new Thread):");
        System.out.println("   Mickey → " + mickey.getState() + " | isAlive: " + mickey.isAlive());
        System.out.println("   Minnie → " + minnie.getState() + " | isAlive: " + minnie.isAlive());
        System.out.println("   📝 Els fils estan creats però NO iniciats.\n");
        Thread.sleep(1000);


        // ════════════════════════════════════════════════════════════
        // 2️⃣ ESTAT: RUNNABLE (després de start)
        // ════════════════════════════════════════════════════════════
        mickey.start();
        minnie.start();
        System.out.println("2️⃣ ESTAT DESPRÉS DE start():");
        System.out.println("   Mickey → " + mickey.getState() + " | isAlive: " + mickey.isAlive());
        System.out.println("   Minnie → " + minnie.getState() + " | isAlive: " + minnie.isAlive());
        System.out.println("   📝 Els fils ja s'estan executant!\n");
        Thread.sleep(500);


        // ════════════════════════════════════════════════════════════
        // 3️⃣ ESTAT: TIMED_WAITING (mentre dormen amb sleep)
        // ════════════════════════════════════════════════════════════
        System.out.println("3️⃣ ESTAT MENTRE DORMEN (sleep):");
        System.out.println("   Mickey → " + mickey.getState() + " | isAlive: " + mickey.isAlive());
        System.out.println("   Minnie → " + minnie.getState() + " | isAlive: " + minnie.isAlive());
        System.out.println("   📝 Ambdós fils estan en TIMED_WAITING (dormint).\n");
        Thread.sleep(2500);


        // ════════════════════════════════════════════════════════════
        // 4️⃣ ESTAT MIXT: Un ha acabat, l'altre encara dorm
        // ════════════════════════════════════════════════════════════
        System.out.println("4️⃣ ESTAT DESPRÉS DE 2.5 SEGONS:");
        System.out.println("   Mickey → " + mickey.getState() + " | isAlive: " + mickey.isAlive());
        System.out.println("   Minnie → " + minnie.getState() + " | isAlive: " + minnie.isAlive());
        System.out.println("   📝 Minnie ja ha acabat (TERMINATED), Mickey encara dorm!\n");


        // Esperem que Mickey també acabi
        mickey.join();


        // ════════════════════════════════════════════════════════════
        // 5️⃣ ESTAT: TERMINATED (tots dos han acabat)
        // ════════════════════════════════════════════════════════════
        System.out.println("5️⃣ ESTAT FINAL (després que acabin tots):");
        System.out.println("   Mickey → " + mickey.getState() + " | isAlive: " + mickey.isAlive());
        System.out.println("   Minnie → " + minnie.getState() + " | isAlive: " + minnie.isAlive());
        System.out.println("   📝 Tots dos fils han TERMINAT completament.\n");

    }
}
