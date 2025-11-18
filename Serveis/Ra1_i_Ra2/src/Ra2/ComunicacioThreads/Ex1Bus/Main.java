package Ra2.ComunicacioThreads.Ex1Bus;

public class Main {
    public static void main(String[] args) {
        // Creem un bus amb capacitat per 3 passatgers
        Bus bus = new Bus(3);

        // Creem 10 passatgers que comparteixen el mateix bus
        // Com que el bus només té 3 seients, alguns hauran d'esperar
        for (int i = 1; i <= 10; i++) {
            Thread threadPassatger = new Thread(new Passatger("Passatger " + i, bus));
            threadPassatger.start();
        }
    }
}