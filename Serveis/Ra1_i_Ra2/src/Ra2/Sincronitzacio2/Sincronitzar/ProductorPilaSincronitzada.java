package Ra2.Sincronitzacio2.Sincronitzar;

public class ProductorPilaSincronitzada implements Runnable {
    private MySynchronizedStack pila;

    public ProductorPilaSincronitzada(MySynchronizedStack pila) {
        this.pila = pila;
    }

    @Override
    public void run() {
        while (true) {
            pila.push('*'); // Crida al mètode synchronized - thread-safe
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
