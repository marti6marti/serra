package Ra2.ComunicacioThreads.Ex11PoisonPill;
/**
 * EXERCICI 11: Finalització ordenada amb "poison pill"
 *
 * ENUNCIAT:
 * Afegeix un mecanisme perquè el sistema productor-consumidor pugui acabar
 * el trajecte i notificar tots els threads que no cal seguir esperant.
 *
 * Utilitza un objecte especial o valor sentinel (anomenat "poison pill")
 * per indicar que el sistema es tanca.
 *
 * REQUISITS:
 * - Els threads han de sortir netament sense quedar bloquejats
 * - Quan els productors acaben, han de notificar als consumidors
 * - Els consumidors han de detectar el "poison pill" i sortir ordenadament
 * - Cap thread pot quedar en wait() indefinidament
 *
 * SOLUCIÓ:
 * - Usem un caràcter especial POISON_PILL = '\0' com a sentinel
 * - Afegim un flag "shutdown" a la pila per indicar tancament
 * - Quan els productors acaben, activem shutdown() que desperta tots els threads
 * - Els consumidors detecten el poison pill i surten del bucle
 */
public class SynchMain {
    public static void main(String[] args) {
        System.out.println("=== Sistema amb Poison Pill ===\n");

        SynchStack pila = new SynchStack();

        // Crear productors
        Producer producer1 = new Producer();
        producer1.setStack(pila);
        Thread tProducer1 = new Thread(producer1);

        Producer producer2 = new Producer();
        producer2.setStack(pila);
        Thread tProducer2 = new Thread(producer2);

        // Crear consumidors
        Consumer consumer1 = new Consumer();
        consumer1.setStack(pila);
        Thread tConsumer1 = new Thread(consumer1);

        Consumer consumer2 = new Consumer();
        consumer2.setStack(pila);
        Thread tConsumer2 = new Thread(consumer2);

        // Iniciar tots els threads
        tProducer1.start();
        tProducer2.start();
        tConsumer1.start();
        tConsumer2.start();

        // Thread que gestiona el shutdown
        Thread shutdownThread = new Thread(() -> {
            try {
                // Espera que els productors acabin
                tProducer1.join();
                tProducer2.join();

                System.out.println("\n🛑 Tots els productors han acabat, iniciant shutdown...\n");

                // Activa el shutdown
                pila.shutdown();

                // Espera que els consumidors acabin
                tConsumer1.join();
                tConsumer2.join();

                System.out.println("\n✅ Sistema tancat correctament");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        shutdownThread.start();
    }
}
