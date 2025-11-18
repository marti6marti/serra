package Ra2.Sincronitzacio2.Sincronitzar;

public class ConsumidorPilaSincronitzada implements Runnable {
    private MySynchronizedStack pila;

    public ConsumidorPilaSincronitzada(MySynchronizedStack pila) {
        this.pila = pila;
    }

    @Override
    public void run() {
        while (true) {
            Character c = pila.pop(); // Crida al mètode synchronized - thread-safe
            if (c != null) {
                System.out.println("Consumit: " + c);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
