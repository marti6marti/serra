package Ra2.ComunicacioThreads.Ex2ProductorConsumidor;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int num;
    private SynchStack stack;

    public Producer() {
        this.num = counter.getAndIncrement();
    }

    public void setStack(SynchStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        char c;
        for (int i = 0; i < 200; i++) {
            c = (char) ((Math.random() * 26) + 'A');
            System.out.println("Producer " + num + ": " + c);

            // Pot bloquejar-se aquí si la pila està plena
            this.stack.push(c);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
