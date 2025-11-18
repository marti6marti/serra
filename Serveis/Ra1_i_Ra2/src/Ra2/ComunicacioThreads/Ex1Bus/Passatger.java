package Ra2.ComunicacioThreads.Ex1Bus;

public class Passatger implements Runnable {
    private String nomPassatger;
    private Bus bus;

    public Passatger(String nomPassatger, Bus bus) {
        this.nomPassatger = nomPassatger;
        this.bus = bus;
    }

    public String getNomPassatger() {
        return nomPassatger;
    }

    /**
     * Lògica del passatger:
     * 1. Intenta pujar al bus (es pot bloquejar si està ple)
     * 2. Simula que està al bus durant 1 segon
     * 3. Baixa del bus (notifica a altres passatgers)
     */
    @Override
    public void run() {
        try {
            // Intent de pujar - es pot bloquejar aquí si el bus està ple
            bus.pujarAlBus();

            // Simula el temps que el passatger està al bus
            Thread.sleep(1000);

            // Baixa del bus i notifica a altres
            bus.baixarDelBus();

        } catch (InterruptedException e) {
            System.out.println(nomPassatger + " ha estat interromput.");
            Thread.currentThread().interrupt();
        }
    }
}
