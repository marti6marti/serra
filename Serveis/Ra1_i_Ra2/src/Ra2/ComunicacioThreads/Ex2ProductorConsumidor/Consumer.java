package Ra2.ComunicacioThreads.Ex2ProductorConsumidor;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int num;
    private SynchStack stack;

    public Consumer() {
        this.num = counter.getAndIncrement();
    }

    public void setStack(SynchStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        char c;
        for (int i = 0; i < 200; i++) {
            // Pot bloquejar-se aquí si la pila està buida
            c = this.stack.pop();

            System.out.println("Consumer " + num + ": " + c);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}