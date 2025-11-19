package Transportadora;

public class Main {
    public static void main(String[] args) {
        Cinta cinta = new Cinta(5); // Máximo 5 paquetes


        Thread productor1 = new Thread(new Productor(cinta, 1, 20));
        Thread productor2 = new Thread(new Productor(cinta, 2, 20));

        Thread consumidor1 = new Thread(new Consumidor(cinta, 1, 20));
        Thread consumidor2 = new Thread(new Consumidor(cinta, 2, 20));


        productor1.setPriority(Thread.NORM_PRIORITY);
        productor2.setPriority(Thread.NORM_PRIORITY);
        consumidor1.setPriority(Thread.NORM_PRIORITY);
        consumidor2.setPriority(Thread.NORM_PRIORITY);


        productor1.start();
        productor2.start();
        
        consumidor1.start();
        consumidor2.start();


        try {
            productor1.join();
            productor2.join();
            consumidor1.join();
            consumidor2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== TODOS LOS HILOS HAN TERMINADO ===");
    }
}
