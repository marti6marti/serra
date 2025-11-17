package Ra2.Sincronitzacio.RatoliQueEsperaDpComensaIsAlive;

/**
 * Main que demostra com un RatoliPare espera que els seus fills acabin.
 *
 * IMPORTANT:
 * - Els fills són RatoliStoppable (mengen en bucle infinit)
 * - Cal cridar stopRunning() manualment per aturar-los
 * - El pare esperarà fins que TOTS els fills estiguin aturats
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== EXPERIMENT: PARE ESPERA FILLS ===\n");

        // 1. CREEM EL RATOLÍ PARE
        RatoliPare ratoliPare = new RatoliPare("Ratolí Pare", 3, false);

        // 2. CREEM ELS RATOLINS FILLS (RatoliStoppable)
        // Aquests mengen en bucle fins que els parem
        RatoliStoppable ratoliFill1 = new RatoliStoppable("Fill Petit", 2, false);
        RatoliStoppable ratoliFill2 = new RatoliStoppable("Fill Mitjà", 3, false);
        RatoliStoppable ratoliFill3 = new RatoliStoppable("Fill Gran", 4, false);

        // 3. CREEM ELS THREADS DELS FILLS
        Thread threadFill1 = new Thread(ratoliFill1, "Thread-Fill1");
        Thread threadFill2 = new Thread(ratoliFill2, "Thread-Fill2");
        Thread threadFill3 = new Thread(ratoliFill3, "Thread-Fill3");

        // 4. AFEGIM ELS THREADS DELS FILLS A LA LLISTA DEL PARE
        ratoliPare.getLlistaFills().add(threadFill1);
        ratoliPare.getLlistaFills().add(threadFill2);
        ratoliPare.getLlistaFills().add(threadFill3);

        // 5. CREEM EL THREAD DEL PARE
        Thread threadPare = new Thread(ratoliPare, "Thread-Pare");

        // 6. INICIEM PRIMER ELS FILLS
        System.out.println("▶️  Iniciant els fills...\n");
        threadFill1.start();
        threadFill2.start();
        threadFill3.start();

        // 7. ESPEREM 1 SEGON PERQUÈ ELS FILLS COMENCIN
        // Això evita que el pare comprovi abans que els fills hagin començat
        Thread.sleep(1000);

        // 8. INICIEM EL PARE
        // El pare començarà a comprovar si els fills estan vius
        System.out.println("▶️  Iniciant el pare...\n");
        threadPare.start();

        // 9. DEIXEM QUE ELS FILLS MENGIN UNA ESTONA
        System.out.println("⏳ Deixem que els fills mengin durant 8 segons...\n");
        Thread.sleep(8000);

        // 10. ATUREM ELS FILLS MANUALMENT
        System.out.println("\n🛑 Aturant els fills per deixar que el pare mengi...\n");
        ratoliFill1.stopRunning();
        ratoliFill2.stopRunning();
        ratoliFill3.stopRunning();

        // 11. ESPEREM QUE TOTS ELS THREADS ACABIN
        threadFill1.join();
        threadFill2.join();
        threadFill3.join();
        threadPare.join();

        System.out.println("\n=== TOTS ELS RATOLINS HAN ACABAT ===");
    }
}