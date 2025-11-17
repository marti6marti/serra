package Ra2.Sincronitzacio.RatoliQueEsperaJoin;

/**
 * Main que demostra com un RatoliPareJoin espera els seus fills amb join().
 *
 * DIFERÈNCIA AMB Ex 4:
 * - Mateix comportament
 * - Però el pare usa join() internament (més eficient)
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  EXERCICI 5: ESPERAR FILLS AMB join()         ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // 1. CREEM EL RATOLÍ PARE (amb join)
        RatoliPareJoin ratoliPareJoin = new RatoliPareJoin("Ratolí Pare Join", 3, false);

        // 2. CREEM ELS RATOLINS FILLS (RatoliStoppable)
        RatoliStoppable ratoliFill1 = new RatoliStoppable("Fill A", 2, false);
        RatoliStoppable ratoliFill2 = new RatoliStoppable("Fill B", 2, false);
        RatoliStoppable ratoliFill3 = new RatoliStoppable("Fill C", 2, false);

        // 3. CREEM ELS THREADS DELS FILLS
        Thread threadFill1 = new Thread(ratoliFill1, "Thread-FillA");
        Thread threadFill2 = new Thread(ratoliFill2, "Thread-FillB");
        Thread threadFill3 = new Thread(ratoliFill3, "Thread-FillC");

        // 4. AFEGIM ELS THREADS DELS FILLS A LA LLISTA DEL PARE
        ratoliPareJoin.getLlistaRatolinsJoin().add(threadFill1);
        ratoliPareJoin.getLlistaRatolinsJoin().add(threadFill2);
        ratoliPareJoin.getLlistaRatolinsJoin().add(threadFill3);

        // 5. CREEM EL THREAD DEL PARE
        Thread threadPare = new Thread(ratoliPareJoin, "Thread-PareJoin");

        // 6. INICIEM PRIMER ELS FILLS
        System.out.println("▶️  Iniciant els fills...\n");
        threadFill1.start();
        threadFill2.start();
        threadFill3.start();

        // 7. ESPEREM 1 SEGON PERQUÈ ELS FILLS COMENCIN
        Thread.sleep(1000);

        // 8. INICIEM EL PARE
        // El pare cridarà join() sobre cada fill
        System.out.println("▶️  Iniciant el pare (que usarà join())...\n");
        threadPare.start();

        // 9. DEIXEM QUE ELS FILLS MENGIN UNA ESTONA
        System.out.println("⏳ Deixem que els fills mengin durant 6 segons...\n");
        Thread.sleep(6000);

        // 10. ATUREM ELS FILLS MANUALMENT
        System.out.println("\n🛑 Aturant els fills per deixar que el pare mengi...\n");
        ratoliFill1.stopRunning();
        ratoliFill2.stopRunning();
        ratoliFill3.stopRunning();

        // 11. ESPEREM QUE TOTS ELS THREADS ACABIN
        // Aquí també usem join() per esperar
        threadFill1.join();
        threadFill2.join();
        threadFill3.join();
        threadPare.join();

        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║  TOTS ELS RATOLINS HAN ACABAT                 ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
}
