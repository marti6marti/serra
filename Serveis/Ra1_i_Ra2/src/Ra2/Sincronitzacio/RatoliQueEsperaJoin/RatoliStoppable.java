package Ra2.Sincronitzacio.RatoliQueEsperaJoin;

import Ra2.Sincronitzacio.RatoliAturatQuanBolean.Ratoli;

/**
 * RatoliStoppable hereta de Ratoli però pot ser aturat de manera controlada.
 *
 * CONCEPTE CLAU:
 * - No "matem" el thread forçosament
 * - El thread s'atura SOL quan detecta que estatRatoli és true
 * - Utilitzem un bucle while(!estatRatoli) per controlar l'execució
 */
public class RatoliStoppable extends Ratoli implements Runnable {

    /**
     * Variable de control per aturar el thread.
     * - false = el ratolí continua menjant
     * - true = el ratolí ha de parar
     */
    private boolean estatRatoli = false;

    // Constructors
    public RatoliStoppable(String nom, int tempsQtrigaEnMenjar, boolean haDitMissatge) {
        super(nom, tempsQtrigaEnMenjar, haDitMissatge);
    }

    public RatoliStoppable() {
        super();
    }

    // Getters i Setters
    public boolean isEstatRatoli() {
        return estatRatoli;
    }

    public void setEstatRatoli(boolean estatRatoli) {
        this.estatRatoli = estatRatoli;
    }

    /**
     * Mètode run() sobreescrit.
     *
     * FUNCIONAMENT:
     * 1. Mentre estatRatoli sigui false, el ratolí continua menjant
     * 2. Cada iteració del bucle, el ratolí menja una vegada
     * 3. Quan estatRatoli passa a true, surt del bucle i acaba
     */
    @Override
    public void run() {
        Thread t = Thread.currentThread();

        System.out.println("🐭 [" + t.getName() + "] " + getNom() +
                " ha començat a menjar en bucle!");

        // BUCLE CONTROLAT: mentre no s'indiqui que ha de parar, continua menjant
        while (!estatRatoli) {
            super.menja2(); // Crida al mètode de la classe pare
        }

        // Quan surt del bucle, significa que estatRatoli = true
        System.out.println("🛑 [" + t.getName() + "] " + getNom() +
                " ha rebut l'ordre de parar i deixa de menjar!");
    }

    /**
     * Mètode públic per aturar el ratolí de manera segura.
     */
    public void stopRunning() {
        System.out.println("⏹️  S'ha demanat aturar " + getNom());
        estatRatoli = true;
    }
}