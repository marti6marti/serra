package Ra2.Sincronitzacio.RatoliQueEsperaDpComensaIsAlive;

import java.util.ArrayList;

/**
 * RatoliPare hereta de Ratoli i espera que tots els seus fills acabin
 * abans de començar a menjar ell.
 *
 * CONCEPTE CLAU:
 * - Manté una llista dels threads dels seus fills
 * - Al run(), comprova amb isAlive() si encara estan actius
 * - Mentre algun fill estigui viu, el pare espera
 * - Quan tots han acabat, el pare comença a menjar
 */
public class RatoliPare extends Ratoli implements Runnable {

    /**
     * Llista que conté els threads dels ratolins fills.
     * El pare consultarà aquesta llista per saber si els fills han acabat.
     */
    private ArrayList<Thread> llistaFills = new ArrayList<>();

    // Constructors
    public RatoliPare(String nom, int tempsQtrigaEnMenjar, boolean haDitMissatge) {
        super(nom, tempsQtrigaEnMenjar, haDitMissatge);
    }

    public RatoliPare() {
        super();
    }

    // Getters i Setters
    public ArrayList<Thread> getLlistaFills() {
        return llistaFills;
    }

    public void setLlistaFills(ArrayList<Thread> llistaFills) {
        this.llistaFills = llistaFills;
    }

    /**
     * FUNCIONAMENT:
     * 1. Comprova cada thread fill de la llista
     * 2. Si algun està viu (isAlive() = true), espera 1 segon
     * 3. Repeteix fins que tots els fills hagin acabat
     * 4. Quan tots estan morts (isAlive() = false), el pare menja
     */
    @Override
    public void run() {
        Thread t = Thread.currentThread();

        System.out.println("\n👨 [" + t.getName() + "] " + getNom() + " està esperant que els seus fills acabin de menjar...");

        // Recorrem cada thread fill
        for (Thread threadFill : llistaFills) {
            System.out.println("   🔍 [" + t.getName() + "] Comprovant si " + threadFill.getName() + " encara està viu...");
             // - true: el thread encara s'està executant
             // - false: el thread ha acabat
            while (threadFill.isAlive()) {
                try {
                    // Espera 1 segon abans de tornar a comprovar
                    Thread.sleep(1000);
                    System.out.println("   ⏳ [" + t.getName() + "] " + threadFill.getName() + " encara està menjant, segueixo esperant...");

                } catch (InterruptedException e) {
                    System.out.println("   ⚠️ [" + t.getName() + "] ha estat interromput mentre esperava els fills!");
                    // Restablim l'estat d'interrupció
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            // Quan sortim del while, significa que aquest fill ha acabat
            System.out.println("   ✅ [" + t.getName() + "] " + threadFill.getName() + " ha acabat!");
        }

        // Tots els fills han acabat, ara el pare pot menjar
        System.out.println("\n🎉 [" + t.getName() + "] " + getNom() + " Tots els fills han acabat! Ara començo a menjar jo!\n");

        // El pare menja
        super.menja2();

        System.out.println("\n👍 [" + t.getName() + "] " + getNom() + " ha acabat de menjar!");
    }
}
