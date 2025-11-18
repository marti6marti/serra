package Ra2.ComunicacioThreads.Ex2ProductorConsumidor;

public class Main {
    public static void main(String[] args) {
        SynchStack pila = new SynchStack();

        Producer producer1 = new Producer();
        producer1.setStack(pila);
        Thread tProducer1 = new Thread(producer1);
        tProducer1.start();

        Producer producer2 = new Producer();
        producer2.setStack(pila);
        Thread tProducer2 = new Thread(producer2);
        tProducer2.start();

        Consumer consumer1 = new Consumer();
        consumer1.setStack(pila);
        Thread tConsumer1 = new Thread(consumer1);
        tConsumer1.start();

        Consumer consumer2 = new Consumer();
        consumer2.setStack(pila);
        Thread tConsumer2 = new Thread(consumer2);
        tConsumer2.start();
    }
}