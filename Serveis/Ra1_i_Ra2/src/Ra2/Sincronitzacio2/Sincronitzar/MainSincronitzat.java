package Ra2.Sincronitzacio2.Sincronitzar;

public class MainSincronitzat {
    public static void main(String[] args) throws InterruptedException {
        // Creem la pila sincronitzada
        MySynchronizedStack pila = new MySynchronizedStack();

        // Creem productor i consumidor
        ProductorPilaSincronitzada productor = new ProductorPilaSincronitzada(pila);
        ConsumidorPilaSincronitzada consumidor = new ConsumidorPilaSincronitzada(pila);

        // Creem i llancem els threads
        Thread tProductor = new Thread(productor, "Thread-Productor");
        Thread tConsumidor = new Thread(consumidor, "Thread-Consumidor");

        tProductor.start();
        tConsumidor.start();

        System.out.println("Threads sincronitzats iniciats. Observant...\n");

        // Bucle que mostra l'estat cada segon
        while (true) {
            Thread.sleep(1000);
            System.out.println(pila); // Ara és coherent!
        }
    }
}