package Ra2.ComunicacioThreads.Ex1Bus;

public class Bus {
    private int capacitatSeients;
    private int passatgersOcupantBus;

    public Bus(int capacitatSeients) {
        this.capacitatSeients = capacitatSeients;
        this.passatgersOcupantBus = 0;
    }

    public int getCapacitatSeients() {
        return capacitatSeients;
    }

    public int getPassatgersOcupantBus() {
        return passatgersOcupantBus;
    }

    /**
     * Mètode per pujar al bus.
     * - synchronized: Només un thread pot executar aquest mètode alhora sobre aquesta instància de Bus
     * - while: Comprova la condició després de despertar-se (protecció contra spurious wakeups)
     * - wait(): Allibera el lock i espera fins que algú faci notify()
     */
    public synchronized void pujarAlBus() throws InterruptedException {
        // MENTRE el bus estigui ple, el thread passatger espera
        while (passatgersOcupantBus >= capacitatSeients) {
            // wait() allibera automàticament el lock d'aquest objecte Bus
            // i posa el thread en la llista d'espera (pool) del Bus
            this.wait();
        }

        // Quan es desperta i surt del while, significa que hi ha lloc
        passatgersOcupantBus++;
        System.out.println(Thread.currentThread().getName() + " ha pujat. Seients ocupats: " + passatgersOcupantBus);
    }

    /**
     * Mètode per baixar del bus.
     * - synchronized: Protegeix l'accés a passatgersOcupantBus
     * - notifyAll(): Desperta TOTS els threads que estaven esperant en wait()
     */
    public synchronized void baixarDelBus() {
        passatgersOcupantBus--;
        System.out.println(Thread.currentThread().getName() + " ha baixat. Seients ocupats: " + passatgersOcupantBus);

        // notifyAll() desperta tots els threads que esperen sobre aquest objecte Bus
        this.notifyAll();
    }
}
